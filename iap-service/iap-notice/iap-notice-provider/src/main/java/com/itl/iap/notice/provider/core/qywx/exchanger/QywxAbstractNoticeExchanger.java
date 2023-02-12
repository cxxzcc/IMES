package com.itl.iap.notice.provider.core.qywx.exchanger;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.UUID;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.entity.email.model.StationNotice;
import com.itl.iap.notice.api.enums.SendStatusEnum;
import com.itl.iap.notice.api.enums.SendTypeEnum;
import com.itl.iap.notice.provider.core.qywx.vo.WeChatData;
import com.itl.iap.notice.provider.core.qywx.config.QywxConfig;
import com.itl.iap.notice.provider.exchanger.AbstractNoticeExchanger;
import com.itl.iap.notice.provider.mapper.MsgSendRecordMapper;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.iap.system.api.entity.IapSysUserT;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 企业微信通知适配器类
 */
@Component
public class QywxAbstractNoticeExchanger extends AbstractNoticeExchanger {
    private Logger logger = LoggerFactory.getLogger(QywxAbstractNoticeExchanger.class);
    @Resource
    private MsgSendRecordMapper msgSendRecordMapper;
    @Autowired
    private QywxConfig qywxConfig;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    public static final String MSGTYPE = "text";
    public static final String CONTENT_KEY = "content";

    @Override
    public boolean match(Map<String, Object> notice) {
        if (notice.get("type") == null || !String.valueOf(SendTypeEnum.QYWX.getItem()).equals(notice.get("type").toString())) {
            return false;
        }
        return true;
    }

    @Override
    public boolean exchanger(Map<String, Object> map) throws Exception {
        System.out.println(JSONUtil.toJsonStr(map));
        String userId = (String) map.get("userId");
        System.out.println(userId);
        //获取用户的手机号
        ResponseData<IapSysUserT> userInfByName = userService.getUser(userId);
        IapSysUserT data = userInfByName.getData();
        if (data == null || data.getUserMobile() == null) {
            logger.info("用户未找到手机号");
            return false;
        }
        String mobilePhone = data.getUserMobile();
        String code = (String) map.get("code");
        Map<String, Object> params = (Map<String, Object>) map.get("params");
        Map<String, Object> result = parseMes(code, params);
        MsgType msgType = (MsgType) result.get("msgType");
        MsgPublicTemplate msgPublicTemplate = (MsgPublicTemplate) result.get("msgPublicTemplate");
        String title = result.get("title") == null ? "" : result.get("title").toString();
        String content = (String) result.get("sysNoticeContent");
        //发送消息
        String token = getToken(qywxConfig.getCorpid(), qywxConfig.getCorpsecret());
        //获取userId
        String toUser = getUserId(token, mobilePhone);
        if (StrUtil.isBlank(toUser)) {
            logger.info("企业微信未找到手机号");
            return false;
        }
        content = title + "\n" + content;
        String postData = createPostData(toUser, MSGTYPE, qywxConfig.getAgentid(), CONTENT_KEY, content);
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(postData, headers);
        String response = restTemplate.postForObject(qywxConfig.getSendMessageUrl() + token, entity, String.class);
        logger.info("获取到的token======>" + token);
        logger.info("请求数据======>" + postData);
        logger.info("发送微信的响应数据======>" + response);
        logger.info("send success!");

        //发送记录
        MsgSendRecord msgSendRecord = new MsgSendRecord();
        if (msgType != null) {
            msgSendRecord.setMsgType(msgType.getName());
        }
        msgSendRecord.setSendTime(new Date());
        msgSendRecord.setMsgPublicTemplateId(msgPublicTemplate.getId());
        msgSendRecord.setTitle(title);
        msgSendRecord.setContent(content);
        msgSendRecord.setNoticeTypeCode(msgPublicTemplate.getNoticeTypeCode());
        msgSendRecord.setReceiverUid(userId);
        msgSendRecord.setReceiverName(data.getUserName());
        //发送成功
        msgSendRecord.setStatus(SendStatusEnum.SEND_SUCCESS.getItem());
        msgSendRecord.setId(UUID.uuid32());
        msgSendRecord.setCreateTime(new Date());
        msgSendRecord.setCreateName("系统发送");
        msgSendRecord.setSendType(SendTypeEnum.QYWX.getItem());
        msgSendRecord.setBusinessId((String) map.get("businessId"));
        msgSendRecord.setReferenceOrderNo((String) map.get("referenceOrderNo"));
        msgSendRecordMapper.insert(msgSendRecord);

        return true;
    }


    public String getUserId(String token, String mobile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        Map map = new HashMap();
        map.put("mobile", mobile);
        HttpEntity<String> entity = new HttpEntity<String>(JSONUtil.toJsonStr(map), headers);
        String response = restTemplate.postForObject(qywxConfig.getUserIdByMobile() + token, entity, String.class);
        JSONObject jsonObject = JSONUtil.parseObj(response);
        Integer errcode = (Integer) jsonObject.get("errcode");
        String userid = (String) jsonObject.get("userid");
        return userid;
    }

    public String getToken(String corpId, String corpSecret) throws IOException {
        String access_token = (String) redisTemplate.opsForValue().get("qywx_access_token");
        if (StrUtil.isBlank(access_token)) {
            String response = restTemplate.getForObject(qywxConfig.getGetTokenUrl(), String.class);
            JSONObject jsonObject = JSONUtil.parseObj(response);
            Integer errcode = (Integer) jsonObject.get("errcode");
            access_token = (String) jsonObject.get("access_token");
            Integer expires_in = (Integer) jsonObject.get("expires_in");
            redisTemplate.opsForValue().set("qywx_access_token", access_token, expires_in - 200, TimeUnit.SECONDS);
        }
        return access_token;
    }

    /**
     * 创建POST BODY
     */
    private String createPostData(String touser, String msgtype, int agent_id, String contentKey, String contentValue) throws UnsupportedEncodingException {
        WeChatData weChatData = new WeChatData();
        weChatData.setTouser(touser);
        weChatData.setAgentid(agent_id);
        weChatData.setMsgtype(msgtype);
        Map<Object, Object> content = new HashMap<Object, Object>();
        content.put(contentKey, contentValue);
        weChatData.setText(content);
        return JSONUtil.toJsonStr(weChatData);
    }
}
