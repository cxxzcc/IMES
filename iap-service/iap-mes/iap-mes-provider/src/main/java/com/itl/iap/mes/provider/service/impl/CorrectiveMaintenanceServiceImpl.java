package com.itl.iap.mes.provider.service.impl;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.andon.core.client.service.AndonService;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.dto.RepairRecordDTO;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.service.CorrectiveUserService;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.annotation.ParseState;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.mapper.CheckExecuteMapper;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.UpkeepExecuteMapper;
import com.itl.iap.notice.api.dto.SendMsgDTO;
import com.itl.iap.notice.client.NoticeService;
import com.itl.mes.andon.api.dto.AndonSaveRepairCallBackDTO;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.DeviceService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class CorrectiveMaintenanceServiceImpl {

    @Autowired
    private CorrectiveMaintenanceMapper correctiveMaintenanceMapper;

    @Autowired
    private CheckExecuteMapper checkExecuteMapper;

    @Autowired
    private UpkeepExecuteMapper upkeepExecuteMapper;

    @Autowired
    private AndonService andonService;

    @Autowired
    private CorrectiveUserService correctiveUserService;

    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private NoticeService noticeService;

    @Value("${notice.corrective.code}")
    private String noticeCode;

    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RepairOperateLogService repairOperateLogService;


    @ParseState(Constant.RepairEnum.class)
    public IPage<CorrectiveMaintenance> findList(CorrectiveMaintenance correctiveMaintenance, Integer pageNum, Integer pageSize) {
        correctiveMaintenance.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum, pageSize);
        String timeType = correctiveMaintenance.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            correctiveMaintenance.setStartTime(LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN));
            correctiveMaintenance.setEndTime(LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
        }

        if (StrUtil.isNotBlank(correctiveMaintenance.getRlatedToMe())) {
            correctiveMaintenance.setRlatedToMe(UserUtils.getUserId());
        }
        IPage<CorrectiveMaintenance> ret = correctiveMaintenanceMapper.findList(page, correctiveMaintenance);
        if (ret != null) {
            List<CorrectiveMaintenance> list = ret.getRecords();
            if (list != null && list.size() > 0) {
                //??????CorrectiveMaintenance?????????id??????list?????????
                List<String> ids = list.stream().map(CorrectiveMaintenance::getId).collect(Collectors.toList());
                //??????list????????????id??????getRepairId???????????????????????????????????????????????????????????????????????????
                List<CorrectiveUser> users = correctiveUserService.lambdaQuery().in(CorrectiveUser::getRepairId, ids).list();
                if (users != null && users.size() > 0) {
                    //???List<CorrectiveUser>>?????????RepairId????????????????????????
                    Map<String, List<CorrectiveUser>> userMap = users.stream().collect(Collectors.groupingBy(CorrectiveUser::getRepairId));
                    //??????usermap,??????????????????????????????
                    list.forEach(x -> x.setRepairUsers(userMap.get(x.getId())));
                }
            }
        }
        return ret;
    }

    @Autowired
    private MesFilesServiceImpl mesFilesService;

    @Transactional(rollbackFor = Exception.class)
    public void save(CorrectiveMaintenance correctiveMaintenance) throws CommonException {
        if (StringUtils.isNotBlank(correctiveMaintenance.getUpkeepId())) {
            UpkeepExecute upkeepExecute = upkeepExecuteMapper.selectById(correctiveMaintenance.getUpkeepId());
            if (upkeepExecute != null) {
                upkeepExecute.setState(1);
                upkeepExecute.setEndTime(new Date());
                deviceService.upateState(upkeepExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
                upkeepExecuteMapper.updateById(upkeepExecute);
            }
        }
        if (org.springframework.util.StringUtils.isEmpty(correctiveMaintenance.getCode())) {
            correctiveMaintenance.setCode(new DeviceHandleBO(correctiveMaintenance.getDeviceBo()).getDevice());
        }
        if (StringUtils.isNotBlank(correctiveMaintenance.getCheckId())) {
            CheckExecute checkExecute = checkExecuteMapper.selectById(correctiveMaintenance.getCheckId());
            if (checkExecute != null) {
                checkExecute.setState(1);
                checkExecute.setEndTime(new Date());
                deviceService.upateState(checkExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
                checkExecuteMapper.updateById(checkExecute);
            }
        }
        correctiveMaintenance.setRepairApplicantId(UserUtils.getUserId());
        correctiveMaintenance.setRepairApplicant(UserUtils.getUserName());
        if (correctiveMaintenance.getState() == 0) {
            if (StringUtils.isNotBlank(correctiveMaintenance.getCode())) {
                List<Map<String, Object>> deviceList = correctiveMaintenanceMapper.getDevice(correctiveMaintenance.getCode());
                if (!deviceList.isEmpty()) {
                    correctiveMaintenance.setProductionLine(deviceList.get(0).get("productLineBo") == null ? "" : deviceList.get(0).get("productLineBo").toString());
                    correctiveMaintenance.setStation(deviceList.get(0).get("stationBo") == null ? "" : deviceList.get(0).get("stationBo").toString());
                }
            }

            correctiveMaintenance.setSiteId(UserUtils.getSite());
            if (correctiveMaintenance.getId() != null && correctiveMaintenance.getState() == 0) {
                correctiveMaintenanceMapper.updateById(correctiveMaintenance);
                repairOperateLogService.add(
                        RepairOperateLog.builder()
                                .deviceId(new DeviceHandleBO(UserUtils.getSite(), correctiveMaintenance.getCode()).getBo())
                                .deviceCode(correctiveMaintenance.getCode())
                                .operateType(OperateTypeEnum.UPDATE.getCode())
                                .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                                .orderTypeDesc(OrderTypeEnum.CORRECTIVE.getDesc())
                                .site(UserUtils.getSite())
                                .operateUserId(UserUtils.getUserId())
                                .operateUserName(UserUtils.getUserName())
                                .orderId(correctiveMaintenance.getId())
                                .orderNo(correctiveMaintenance.getRepairNo())
                                .build()
                );
            } else {
                QueryWrapper<CorrectiveMaintenance> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("code", correctiveMaintenance.getCode());
                Integer count = correctiveMaintenanceMapper.selectCount(queryWrapper);
//            if(count > 0){
//                throw new CustomException(CommonCode.CODE_REPEAT);
//            }
                if (org.springframework.util.StringUtils.isEmpty(correctiveMaintenance.getRepairNo())) {
                    ResponseData<String> stringResponseData = codeRuleService.generatorNextNumber("SB_WX");
                    if (!ResultResponseEnum.SUCCESS.getCode().equals(stringResponseData.getCode())) {
                        throw new CommonException(stringResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
                    }
                    correctiveMaintenance.setRepairNo(stringResponseData.getData());
                }
                correctiveMaintenance.setCreateTime(new Date());
                correctiveMaintenanceMapper.insert(correctiveMaintenance);
                sendNotice(correctiveMaintenance);
                repairOperateLogService.add(
                        RepairOperateLog.builder()
                                .deviceId(new DeviceHandleBO(UserUtils.getSite(), correctiveMaintenance.getCode()).getBo())
                                .deviceCode(correctiveMaintenance.getCode())
                                .operateType(OperateTypeEnum.UPDATE.getCode())
                                .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                                .orderTypeDesc(OrderTypeEnum.CORRECTIVE.getDesc())
                                .site(UserUtils.getSite())
                                .operateUserId(UserUtils.getUserId())
                                .operateUserName(UserUtils.getUserName())
                                .orderId(correctiveMaintenance.getId())
                                .orderNo(correctiveMaintenance.getRepairNo())
                                .build()
                );

                //????????????????????????????????????
                if (StrUtil.isNotBlank(correctiveMaintenance.getAndonBo())) {
                    AndonSaveRepairCallBackDTO andonSaveRepairCallBackDTO = new AndonSaveRepairCallBackDTO();
                    andonSaveRepairCallBackDTO.setAndonBo(correctiveMaintenance.getAndonBo());
                    andonSaveRepairCallBackDTO.setRepairNo(correctiveMaintenance.getRepairNo());
                    andonService.saveRepairCallBack(andonSaveRepairCallBackDTO);
                }

            }
            saveFile(correctiveMaintenance);
            saveUsers(correctiveMaintenance);
            deviceService.upateState(correctiveMaintenance.getCode(), UserUtils.getSite(), DeviceStateEnum.DWX.getCode());
        }
    }

    private void saveUsers(CorrectiveMaintenance correctiveMaintenance) {
        List<CorrectiveUser> users = correctiveMaintenance.getRepairUsers();
        String id = correctiveMaintenance.getId();
        correctiveUserService.remove(new QueryWrapper<CorrectiveUser>().lambda().eq(CorrectiveUser::getRepairId, id));
        if (users != null && users.size() > 0) {
            users.forEach(x -> x.setRepairId(id));
            correctiveUserService.saveBatch(users);
        }
    }

    private void sendNotice(CorrectiveMaintenance correctiveMaintenance) {
        //?????????????????????,??????????????????
        List<CorrectiveUser> users = correctiveMaintenance.getRepairUsers();
        if (CollUtil.isEmpty(users)) {
            return;
        }
        String userNameList = users.stream().map(CorrectiveUser::getRepairUserName).collect(Collectors.joining(","));
        Map<String, Object> templateParams = new HashMap<>(16);
        templateParams.put("orderNo", correctiveMaintenance.getRepairNo());
        templateParams.put("deviceCode", correctiveMaintenance.getCode());
        templateParams.put("faultCode", correctiveMaintenance.getFaultCode());
        templateParams.put("userList", userNameList);
        ThreadUtil.execAsync(() -> {
            for (CorrectiveUser user : users) {
                SendMsgDTO sendMsgDTO = SendMsgDTO.builder()
                        .businessId(correctiveMaintenance.getId())
                        .referenceOrderNo(correctiveMaintenance.getRepairNo())
                        .code(noticeCode)
                        .params(templateParams)
                        .userId(user.getRepairUserAcount())
                        .userName(user.getRepairUserName())
                        .build();
                noticeService.sendMsg(sendMsgDTO);
            }
        });
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveFile(CorrectiveMaintenance correctiveMaintenance) {
        String id = correctiveMaintenance.getId();
        LambdaQueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().lambda().eq(MesFiles::getGroupId, id).in(MesFiles::getFileType, Arrays.asList("pics", "videos"));
        if (correctiveMaintenance.getMesFiles() != null && !correctiveMaintenance.getMesFiles().isEmpty()) {
            List<MesFiles> pics = correctiveMaintenance.getMesFiles().get("pics");
            List<MesFiles> videos = correctiveMaintenance.getMesFiles().get("videos");
            if (pics != null && pics.size() > 0) {
                pics.forEach(file -> {
                    query.ne(MesFiles::getId, file.getId());
                    file.setGroupId(id);
                    file.setFileType("pic");
                });
                mesFilesService.updateBatchById(pics);
            }
            if (videos != null && videos.size() > 0) {
                videos.forEach(file -> {
                    query.ne(MesFiles::getId, file.getId());
                    file.setGroupId(id);
                    file.setFileType("video");
                });
                mesFilesService.updateBatchById(videos);
            }
        }
        List<MesFiles> mesFiles = mesFilesService.list(query);
        if (mesFiles != null && mesFiles.size() > 0) {
            mesFilesService.removeByIds(mesFiles.stream().map(MesFiles::getId).collect(Collectors.toList()));
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            correctiveUserService.remove(new QueryWrapper<CorrectiveUser>().lambda().in(CorrectiveUser::getRepairId, ids));
            correctiveMaintenanceMapper.deleteBatchIds(ids);
        }
    }

    public CorrectiveMaintenance getById(String id) {
        CorrectiveMaintenance correctiveMaintenance = correctiveMaintenanceMapper.selectById(id);
        List<MesFiles> list = mesFilesService.lambdaQuery().eq(MesFiles::getGroupId, id).list();
        if (list != null && list.size() > 0) {
            Map<String, List<MesFiles>> listMap = list.stream().collect(Collectors.groupingBy(MesFiles::getFileType));
            Map<String, List<MesFiles>> map = new HashMap<>();
            map.put("pics", listMap.get("pic"));
            map.put("videos", listMap.get("video"));
            correctiveMaintenance.setMesFiles(map);
        }

        List<CorrectiveUser> correctiveUserList = correctiveUserService.list(new QueryWrapper<CorrectiveUser>().lambda().eq(CorrectiveUser::getRepairId, id));
        correctiveMaintenance.setRepairUsers(correctiveUserList);
        return correctiveMaintenance;
    }


    /**
     * ??????????????????
     *
     * @param params code=???????????????????????????
     * @return ??????????????????
     */
    public Page<RepairRecordDTO> repairRecord(Map<String, Object> params) {
        Page<RepairRecordDTO> queryPage = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        List<RepairRecordDTO> list = correctiveMaintenanceMapper.repairRecord(queryPage, params);
        queryPage.setRecords(list);
        return queryPage;

    }

    /**
     * ????????????????????????????????????
     *
     * @param site ??????id
     * @return ???????????????????????????
     */
    public Integer getAllRepairCount(String site) {
        if (StrUtil.isBlank(site)) {
            return 0;
        }
        return correctiveMaintenanceMapper.getAllRepairCount(site);
    }

    /**
     * ????????????id?????????id????????????????????????
     *
     * @param userId ??????id
     * @param site   ??????id
     * @return ????????????????????????
     */
    public Integer getRepairCountByUser(String userId, String site) {
        if (StrUtil.isBlank(site) || StrUtil.isBlank(userId)) {
            return 0;
        }
        return correctiveMaintenanceMapper.getRepairCountByUser(userId, site);
    }
}
