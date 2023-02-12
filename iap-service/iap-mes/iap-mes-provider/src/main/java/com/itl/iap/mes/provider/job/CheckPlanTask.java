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
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.mes.api.common.CheckStateEnum;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.mapper.CheckExecuteMapper;
import com.itl.iap.mes.provider.mapper.CheckPlanMapper;
import com.itl.iap.mes.provider.mapper.JobMapper;
import com.itl.iap.mes.provider.service.impl.CheckDeviceServiceImpl;
import com.itl.iap.mes.provider.service.impl.CheckExecuteServiceImpl;
import com.itl.iap.mes.provider.service.impl.CheckPlanUserServiceImpl;
import com.itl.iap.notice.api.dto.SendMsgDTO;
import com.itl.iap.notice.client.NoticeService;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.bo.CodeRuleHandleBO;
import com.itl.mes.core.api.dto.CodeGenerateDto;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("checkPlan")
@EnableScheduling
@Slf4j
@RequiredArgsConstructor
public class CheckPlanTask implements ITask {

    final private CheckExecuteMapper checkExecuteMapper;
    final private CheckExecuteServiceImpl checkExecuteService;
    final private CheckPlanMapper checkPlanMapper;
    final private CheckDeviceServiceImpl checkDeviceService;
    final private DeviceService deviceService;
    final private UserService userService;
    final private CodeRuleService codeRuleService;
    final private CheckPlanUserServiceImpl checkPlanUserService;
    final private NoticeService noticeService;
    final private RepairOperateLogService repairOperateLogService;
    final private JobMapper jobMapper;

    @Value("${notice.checkPlan.code}")
    private String noticeCode;

    @Transactional
    @Override
    public void run(String params) {
        try {
            if (StrUtil.isBlank(params)) {
                log.warn("工单生成参数不能为空");
                return;
            }
            JSONObject map = JSONUtil.parseObj(params);
            String planId = map.getStr("checkPlan");
            String jobId = map.getStr("jobId");
            String operaStartTime = map.getStr("operaStartTime");
            String operaEndTime = map.getStr("operaEndTime");
            String isEnd = map.getStr("isEnd");
            if (StrUtil.isBlank(planId)) {
                log.warn("计划id不能为空");
                return;
            }
            CheckPlan plan = checkPlanMapper.selectById(planId);
            //如果计划暂停 停止生成工单
            if (0 == plan.getState()) {
                log.warn("计划{0}暂停 停止生成工单", plan.getId());
                return;
            }
            //计划设备
            List<CheckDevice> deviceInfos = checkDeviceService.getDeviceInfos(planId);
            ResponseData<List<String>> checkNos = codeRuleService.generatorNextNumberList(
                    CodeGenerateDto.builder().codeRuleBo(new CodeRuleHandleBO(plan.getSiteId(), "SB_DJ").getBo())
                            .number(deviceInfos.size())
                            .paramMap(null).build()
            );
            if (!ResultResponseEnum.SUCCESS.getCode().equals(checkNos.getCode())) {
                log.warn("计划{0}工单号获取失败", plan.getId());
                return;
            }
            List<String> nos = checkNos.getData();
            //计划点检人
            List<CheckPlanUser> checkPlanUsers = checkPlanUserService.listByCheckPlanId(planId);
            List userNameList = checkPlanUsers.stream().map(CheckPlanUser::getCheckUserId).collect(Collectors.toList());
            ResponseData<List<IapSysUserT>> userList = userService.getUserList(userNameList);
            List<IapSysUserT> userInfoList = userList.getData();
            Map<String, IapSysUserT> userInfoMap = userInfoList.stream().collect(Collectors.toMap(IapSysUserT::getUserName, Function.identity()));
            String realNameList = userInfoList.stream().map(IapSysUserT::getRealName).collect(Collectors.joining(","));

            List<String> collect = deviceInfos.stream().map(CheckDevice::getDeviceId).collect(Collectors.toList());
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(collect);
            ResponseData<List<DevicePlusVo>> response = deviceService.getDataByCondition(conditionDto);
            List<DevicePlusVo> devices = new ArrayList<>();
            if (response != null && ResultResponseEnum.SUCCESS.getCode().equals(response.getCode())) {
                devices = response.getData();
            }
            Map<String, DevicePlusVo> deviceMap = devices.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            for (int i = 0; i < deviceInfos.size(); i++) {
                CheckDevice deviceInfo = deviceInfos.get(i);
                String deviceId = deviceInfo.getDeviceId();
                if (deviceMap.containsKey(deviceId)) {
                    DevicePlusVo device = deviceMap.get(deviceId);
                    CheckExecute checkExecute = new CheckExecute();
                    checkExecute.setProductionLine(device.getProductLineBo());
                    //此时将待点检工单改成过期
                    checkExecuteService.updateCheckExecuteStateByDeviceId(planId,ListUtil.toList(deviceId), plan.getCreateBy(), CheckStateEnum.YGQ.getCode());
                    //结束标识不继续生成
                    if ("1".equals(isEnd)) {
                        continue;
                    }
                    checkExecute.setOperaStartTime(DateUtil.parse(operaStartTime, DatePattern.NORM_DATETIME_PATTERN));
                    checkExecute.setOperaEndTime(DateUtil.parse(operaEndTime, DatePattern.NORM_DATETIME_PATTERN));
                    checkExecute.setCheckPlanId(plan.getId());
                    checkExecute.setCheckPlanName(plan.getCheckPlanName());
                    checkExecute.setDeviceId(deviceId);
                    checkExecute.setDataCollectionId(plan.getDataCollectionId());
                    checkExecute.setSiteId(plan.getSiteId());
                    checkExecute.setDataCollectionName(plan.getDataCollectionName());
                    checkExecute.setState(CheckStateEnum.DDZ.getCode());
                    checkExecute.setCheckNo(nos.get(i));
                    checkExecute.setCreateBy(plan.getCreateBy() == null ? "admin" : plan.getCreateBy());
                    checkExecute.setUpdateBy(plan.getCreateBy() == null ? "admin" : plan.getCreateBy());
                    checkExecute.setJobId(jobId);
                    checkExecuteMapper.insert(checkExecute);
                    //修改设备状态 缺少事务
//                    deviceService.upateState(device.getDevice(), plan.getSiteId(), DeviceStateEnum.DDJ.getCode());
                    repairOperateLogService.add(
                            RepairOperateLog.builder()
                                    .operateType(OperateTypeEnum.ADD.getCode())
                                    .orderType(OrderTypeEnum.CHECK.getCode())
                                    .site(plan.getSiteId())
                                    .operateUserId("系统")
                                    .operateUserName("系统")
                                    .orderId(checkExecute.getId())
                                    .orderNo(checkExecute.getCheckNo())
                                    .build()
                    );
                    //获取点检人信息,发送消息通知
                    Map<String, Object> templateParams = new HashMap<>(16);
                    templateParams.put("orderNo", checkExecute.getCheckNo());
                    templateParams.put("deviceCode", device.getDevice());
                    templateParams.put("userList", realNameList);
                    ThreadUtil.execAsync(() -> {
                        for (CheckPlanUser checkPlanUser : checkPlanUsers) {
                            String checkUserId = checkPlanUser.getCheckUserId();
                            String realName = "";
                            if (userInfoMap.containsKey(checkUserId)) {
                                realName = userInfoMap.get(checkUserId).getRealName();
                            }
                            SendMsgDTO sendMsgDTO = SendMsgDTO.builder()
                                    .businessId(checkExecute.getId())
                                    .code(noticeCode)
                                    .params(templateParams)
                                    .userId(checkUserId)
                                    .userName(realName)
                                    .referenceOrderNo(checkExecute.getCheckNo())
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
