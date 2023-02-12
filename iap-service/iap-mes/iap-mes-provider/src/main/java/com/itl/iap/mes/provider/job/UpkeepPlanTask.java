package com.itl.iap.mes.provider.job;


import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.mes.api.common.CheckStateEnum;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.common.UpkeepStateEnum;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.JobMapper;
import com.itl.iap.mes.provider.mapper.UpkeepExecuteMapper;
import com.itl.iap.mes.provider.mapper.UpkeepPlanMapper;
import com.itl.iap.mes.provider.service.impl.UpkeepDeviceServiceImpl;
import com.itl.iap.mes.provider.service.impl.UpkeepExecuteServiceImpl;
import com.itl.iap.mes.provider.service.impl.UpkeepUserServiceImpl;
import com.itl.iap.notice.api.dto.SendMsgDTO;
import com.itl.iap.notice.client.NoticeService;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("upkeepPlan")
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class UpkeepPlanTask implements ITask {

    final private UpkeepExecuteServiceImpl upkeepExecuteService;
    final private UpkeepExecuteMapper upkeepExecuteMapper;
    final private UpkeepPlanMapper upkeepPlanMapper;
    final private UpkeepDeviceServiceImpl upkeepDeviceService;
    final private CorrectiveMaintenanceMapper correctiveMaintenanceMapper;
    final private DeviceService deviceService;
    final private CodeRuleService codeRuleService;
    final private UpkeepUserServiceImpl upkeepUserService;
    final private NoticeService noticeService;
    final private RepairOperateLogService repairOperateLogService;
    final private UserService userService;
    final private JobMapper jobMapper;


    @Value("${notice.upkeepPlan.code}")
    private String noticeCode;

    @Transactional
    @Override
    public void run(String params) {
        try {
            log.info("==============================开始生成工单==============================");
            if (StrUtil.isBlank(params)) {
                log.info("参数为空");
                return;
            }
            JSONObject map = JSONUtil.parseObj(params);
            String planId = map.getStr("upkeepPlan");
            String jobId = map.getStr("jobId");
            String operaStartTime = map.getStr("operaStartTime");
            String operaEndTime = map.getStr("operaEndTime");
            String isEnd = map.getStr("isEnd");
            if (StrUtil.isBlank(planId)) {
                log.warn("计划id为空");
                return;
            }
            //获取计划详情
            UpkeepPlan plan = upkeepPlanMapper.selectById(planId);
            //如果计划暂停 停止生成工单
            if (0 == plan.getState()) {
                log.warn("计划{0}暂停 停止生成工单", plan.getId());
                return;
            }
            //获取工单设备列表
            List<UpkeepDevice> deviceInfos = upkeepDeviceService.getDeviceInfos(planId);
            //生成保养工单号
            ResponseData<List<String>> upkeepNos = codeRuleService.generatorNextNumberList(
                    CodeGenerateDto.builder().codeRuleBo(new CodeRuleHandleBO(plan.getSite(), "SB_BY").getBo())
                            .number(deviceInfos.size())
                            .paramMap(null).build()
            );
            if (!ResultResponseEnum.SUCCESS.getCode().equals(upkeepNos.getCode())) {
                log.warn("生成保养工单号失败");
                return;
            }
            List<String> nos = upkeepNos.getData();
            //获取保养人列表
            List<UpkeepUser> upkeepUsers = upkeepUserService.listByUpkeepPlanId(planId);
            List userNameList = upkeepUsers.stream().map(UpkeepUser::getUpkeepUserId).collect(Collectors.toList());
            ResponseData<List<IapSysUserT>> userList = userService.getUserList(userNameList);
            List<IapSysUserT> userInfoList = userList.getData();
            Map<String, IapSysUserT> userInfoMap = userInfoList.stream().collect(Collectors.toMap(IapSysUserT::getUserName, Function.identity()));
            String realNameList = userInfoList.stream().map(IapSysUserT::getRealName).collect(Collectors.joining(","));

            List<String> collect = deviceInfos.stream().map(UpkeepDevice::getDeviceId).collect(Collectors.toList());
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(collect);
            ResponseData<List<DevicePlusVo>> response = deviceService.getDataByCondition(conditionDto);
            List<DevicePlusVo> devices = new ArrayList<>();
            if (response != null && ResultResponseEnum.SUCCESS.getCode().equals(response.getCode())) {
                devices = response.getData();
            }
            Map<String, DevicePlusVo> deviceMap = devices.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            //生成保养工单
            for (int i = 0; i < deviceInfos.size(); i++) {
                UpkeepDevice deviceInfo = deviceInfos.get(i);
                String deviceId = deviceInfo.getDeviceId();
                if (deviceMap.containsKey(deviceId)) {
                    DevicePlusVo device = deviceMap.get(deviceId);
                    UpkeepExecute upkeepExecute = new UpkeepExecute();
                    upkeepExecute.setProductionLine(device.getProductLineBo());
                    //此时将待保养工单改成过期
                    upkeepExecuteService.updateCheckExecuteStateByDeviceId(planId, ListUtil.toList(deviceId), plan.getCreateBy(), UpkeepStateEnum.YGQ.getCode());
                    //结束标识不继续生成
                    if ("1".equals(isEnd)) {
                        continue;
                    }
                    upkeepExecute.setOperaStartTime(DateUtil.parse(operaStartTime, DatePattern.NORM_DATETIME_PATTERN));
                    upkeepExecute.setOperaEndTime(DateUtil.parse(operaEndTime, DatePattern.NORM_DATETIME_PATTERN));
                    upkeepExecute.setUpkeepPlanId(plan.getId());
                    upkeepExecute.setUpkeepPlanName(plan.getUpkeepPlanName());
                    upkeepExecute.setDeviceId(deviceId);
                    upkeepExecute.setDataCollectionId(plan.getDataCollectionId());
                    upkeepExecute.setSite(plan.getSite());
                    upkeepExecute.setState(UpkeepStateEnum.DBY.getCode());
                    upkeepExecute.setUpkeepNo(nos.get(i));
                    upkeepExecute.setCreateBy(plan.getCreateBy() == null ? "admin" : plan.getCreateBy());
                    upkeepExecute.setUpdateBy(plan.getCreateBy() == null ? "admin" : plan.getCreateBy());
                    upkeepExecute.setJobId(jobId);
                    upkeepExecuteMapper.insert(upkeepExecute);
                    repairOperateLogService.add(
                            RepairOperateLog.builder()
                                    .operateType(OperateTypeEnum.ADD.getCode())
                                    .orderType(OrderTypeEnum.UPKEEP.getCode())
                                    .site(plan.getSite())
                                    .operateUserId("系统")
                                    .operateUserName("系统")
                                    .orderId(upkeepExecute.getId())
                                    .orderNo(upkeepExecute.getUpkeepNo())
                                    .build()
                    );
                    //获取保养人信息,发送消息通知
                    Map<String, Object> templateParams = new HashMap<>(16);
                    templateParams.put("orderNo", upkeepExecute.getUpkeepNo());
                    templateParams.put("deviceCode", device.getDevice());
                    templateParams.put("userList", realNameList);
                    ThreadUtil.execAsync(() -> {
                        for (UpkeepUser upkeepUser : upkeepUsers) {
                            String upkeepUserId = upkeepUser.getUpkeepUserId();
                            String realName = "";
                            if (userInfoMap.containsKey(upkeepUserId)) {
                                realName = userInfoMap.get(upkeepUserId).getRealName();
                            }
                            SendMsgDTO sendMsgDTO = SendMsgDTO.builder()
                                    .businessId(upkeepExecute.getId())
                                    .code(noticeCode)
                                    .params(templateParams)
                                    .userId(upkeepUserId)
                                    .userName(realName)
                                    .referenceOrderNo(upkeepExecute.getUpkeepNo())
                                    .build();
                            noticeService.sendMsg(sendMsgDTO);
                        }
                    });
                }
            }
            //工单生成修改状态
            if (StrUtil.isNotBlank(jobId)) {
                ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
                scheduleJobEntity.setIsExecute("1");
                scheduleJobEntity.setJobId(Long.parseLong(jobId));
                jobMapper.updateById(scheduleJobEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
