package com.itl.iap.notice.provider.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.notice.api.dto.MsgTypeDto;
import com.itl.iap.notice.api.dto.SendMsgDTO;
import com.itl.iap.notice.api.entity.MsgPublicTemplate;
import com.itl.iap.notice.api.entity.MsgSendRecord;
import com.itl.iap.notice.api.entity.MsgType;
import com.itl.iap.notice.api.pojo.ResponseCode;
import com.itl.iap.notice.api.service.MsgPublicTemplateService;
import com.itl.iap.notice.api.service.MsgTypeService;
import com.itl.iap.notice.api.service.NoticeService;
import com.itl.iap.notice.provider.core.kafka.KafkaProducer;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 发送通知Controller
 *
 * @author 曾慧任
 * @date 2020-06-29
 * @since jdk1.8
 */
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeControllerImpl {
    @Value("${notice.enableMq}")
    private boolean noticeEnableMq;

    @Autowired(required = false)
    private KafkaProducer kafkaProducer;

    @Autowired
    private MsgPublicTemplateService msgPublicTemplateService;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MsgTypeService msgTypeService;

    @ApiOperation(value = "发送消息", notes = "发送消息")
    @PostMapping(value = "/sendMessage")
    public ResponseData sendMessage(@RequestBody Map<String, Object> notice) {
        System.out.println(noticeEnableMq);
        if (noticeEnableMq) {
            log.info("user mq send");
            kafkaProducer.sendMessage(notice);
        } else {
            log.info("don't use mq send");
            noticeService.sendMessage(notice);
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    @ApiOperation(value = "发送消息", notes = "发送消息")
    @PostMapping(value = "/sendMsg")
    public ResponseData sendMsg(@RequestBody SendMsgDTO notice) {
        System.out.println(noticeEnableMq);
        if (noticeEnableMq) {
            log.info("user mq send");
            kafkaProducer.sendMessage(JSONUtil.parseObj(notice));
        } else {
            log.info("don't use mq send");
            noticeService.sendMessage(JSONUtil.parseObj(notice));
        }
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
    }

    @GetMapping(value = "/code/{code}")
    @ApiOperation(value = "根据编码查询", notes = "根据编码查询")
    public ResponseData<MsgPublicTemplate> getMsgPublicTemplateByCode(String code) {
        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgPublicTemplateService.getMsgPublicTemplateByCode(code));
    }


    @ApiOperation(value = "更改状态为已读", notes = "更改状态为已读")
    @GetMapping(value = "/updateReadFlag/{id}")
    public ResponseData updateReadFlag(@PathVariable("id") String id) {
        noticeService.updateReadFlag(id);
        return ResponseData.success();
    }

    @ApiOperation(value = "用户获取动态卡片", notes = "用户获取动态卡片")
    @PostMapping(value = "/dynamicMsgTypes")
    public ResponseData<List<MsgType>> dynamicMsgTypes() {
        log.info("用户获取动态卡片");
        List<MsgType> msgTypeList = null;
        try {
            MsgTypeDto msgTypeDto = new MsgTypeDto();
            msgTypeDto.setReceiverUid("-1");
            msgTypeList = msgTypeService.selectDynamicMsgTypes(msgTypeDto);
        } catch (Exception e) {
            log.error("获取动态卡片失败：" + e.getMessage());
            e.printStackTrace();
            return new ResponseData<>(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage());
        }

        return new ResponseData<>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), msgTypeList);
    }

    @ApiOperation(value = "根据消息类型编码获取当前用户的消息记录", notes = "根据消息类型编码获取当前用户的消息记录")
    @GetMapping(value = "/messageList/{code}")
    public ResponseData<List<MsgSendRecord>> getMessageList(String code) {
        return null;
    }

    @ApiOperation(value = "查询系统发送未读数量", notes = "查询系统发送未读数量")
    @PostMapping("/msgSendRecordLlist")
    public ResponseData<Map> getMsgSendRecordListByUser(String userId, Integer page, Integer pageSize) {
        Map map = noticeService.getMsgSendRecordListByUser(userId, page, pageSize);
        return new ResponseData<Map>(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), map);
    }

    @GetMapping("/unreadCount")
    @ApiOperation(value = "未读消息数量")
    public ResponseData<Integer> getUnreadCountByUser(String userId) {
        return ResponseData.success(noticeService.getUnreadCountByUser(StrUtil.isBlank(userId) ? UserUtils.getUserId() : userId));
    }

    @GetMapping("/unreadCountGroupByType")
    @ApiOperation(value = "未读消息数量根据消息类型分组")
    public ResponseData<Map<String, Object>> getUnreadCountGroupByType(String userId) {
        return ResponseData.success(noticeService.getUnreadCountGroupByType(StrUtil.isBlank(userId) ? UserUtils.getUserId() : userId));
    }
}
