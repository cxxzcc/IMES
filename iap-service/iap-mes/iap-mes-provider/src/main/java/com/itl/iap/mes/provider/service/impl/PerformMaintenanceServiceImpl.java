package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.common.CorrectiveStateEnum;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageDetailRequestDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.SparePartStorageRequestDTO;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.service.CorrectiveUserService;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.mapper.CorrectiveMaintenanceMapper;
import com.itl.iap.mes.provider.mapper.FaultMapper;
import com.itl.iap.system.api.dto.IapSysUserTDto;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.dto.AbnormalCountStatisticsDTO;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.api.vo.ProductLineVo;
import com.itl.mes.core.api.vo.StationVo;
import com.itl.mes.core.client.service.DeviceService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PerformMaintenanceServiceImpl {
    @Autowired
    private CorrectiveMaintenanceMapper correctiveMaintenanceMapper;
    @Autowired
    private SparePartStorageRecordServiceImpl sparePartStorageRecordService;
    @Autowired
    private FaultMapper faultMapper;
    @Autowired
    private CorrectiveUserService correctiveUserService;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private MesFilesServiceImpl mesFilesService;
    @Autowired
    private RepairOperateLogService repairOperateLogService;
    @Autowired
    private UserService userService;


    public CorrectiveMaintenance getWorkOrderById(String id) {
        CorrectiveMaintenance correctiveMaintenance = correctiveMaintenanceMapper.selectById(id);
        ProductLineVo productLineVo = correctiveMaintenanceMapper.getProductLineByBo(correctiveMaintenance.getProductionLine());
        if (productLineVo != null) {
            correctiveMaintenance.setProductionLineName(productLineVo.getProductLine());
        }
        StationVo stationVo = correctiveMaintenanceMapper.getStationByBo(correctiveMaintenance.getStation());
        if (stationVo != null) {
            correctiveMaintenance.setStationName(stationVo.getStationName());
        }
        ResponseData<DeviceVo> deviceVoByDevice = deviceService.getDeviceVoByDevice(correctiveMaintenance.getCode());
        DeviceVo data = deviceVoByDevice.getData();
        if (data != null) {
            correctiveMaintenance.setDeviceModel(data.getDeviceModel());
            correctiveMaintenance.setDeviceName(data.getDeviceName());
            List<DeviceTypeSimplifyVo> assignedDeviceTypeList = data.getAssignedDeviceTypeList();
            if (CollUtil.isNotEmpty(assignedDeviceTypeList)) {
                correctiveMaintenance.setDeviceType(assignedDeviceTypeList.stream().map(DeviceTypeSimplifyVo::getDeviceTypeName).collect(Collectors.joining(",")));
            }
        }
        Fault fault = faultMapper.selectOne(new QueryWrapper<Fault>().eq("faultCode", correctiveMaintenance.getFaultCode()));
        if (fault != null) {
            correctiveMaintenance.setFaultDesc(fault.getRemark());
            // 更改原因: 安灯触发不需要
//            correctiveMaintenance.setRepairMethod(fault.getRepairMethod());
            correctiveMaintenance.setErrorTypeName(fault.getType());
        }
        List<MesFiles> list = mesFilesService.lambdaQuery().eq(MesFiles::getGroupId, id).list();
        if (list != null && list.size() > 0) {
            Map<String, List<MesFiles>> listMap = list.stream().collect(Collectors.groupingBy(MesFiles::getFileType));
            Map<String, List<MesFiles>> map = new HashMap<>();
            map.put("pics", listMap.get("pic"));
            map.put("videos", listMap.get("video"));
            map.put("wxhpics", listMap.get("wxhpic"));
            map.put("wxhvideos", listMap.get("wxhvideo"));

            correctiveMaintenance.setMesFiles(map);
        }
        List<CorrectiveUser> correctiveUserList = correctiveUserService.list(new QueryWrapper<CorrectiveUser>().lambda().eq(CorrectiveUser::getRepairId, id));
        correctiveMaintenance.setRepairUsers(correctiveUserList);

        List<SparePartStorageRecordDTO> sparePartStorageRecordDTOS = sparePartStorageRecordService.detailListByReferenceNo(correctiveMaintenance.getRepairNo());
        if (CollUtil.isNotEmpty(sparePartStorageRecordDTOS)) {
            List<SparePartStorageDetailRequestDTO> list1 = new ArrayList<SparePartStorageDetailRequestDTO>();
            for (SparePartStorageRecordDTO sparePartStorageRecordDTO : sparePartStorageRecordDTOS) {
                SparePartStorageDetailRequestDTO sparePartStorageDetailRequestDTO = new SparePartStorageDetailRequestDTO();
                sparePartStorageDetailRequestDTO.setType(sparePartStorageRecordDTO.getSparePartTypeDesc());
                sparePartStorageDetailRequestDTO.setCount(sparePartStorageRecordDTO.getOutCount());
                sparePartStorageDetailRequestDTO.setSparePartName(sparePartStorageRecordDTO.getSparePartName());
                sparePartStorageDetailRequestDTO.setWareHouseName(sparePartStorageRecordDTO.getWareHouseName());
                sparePartStorageDetailRequestDTO.setInventory(sparePartStorageRecordDTO.getInventory());
                sparePartStorageDetailRequestDTO.setSparePartNo(sparePartStorageRecordDTO.getSparePartNo());
                list1.add(sparePartStorageDetailRequestDTO);
            }
            correctiveMaintenance.setDetails(list1);

        }
        return correctiveMaintenance;
    }


    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public void save(CorrectiveMaintenance correctiveMaintenance) throws CommonException {
        String site = UserUtils.getSite();
        correctiveMaintenance.setSiteId(UserUtils.getSite());
        Integer count = correctiveUserService.lambdaQuery()
                .eq(CorrectiveUser::getRepairUserId, UserUtils.getUserId())
                .eq(CorrectiveUser::getRepairId, correctiveMaintenance.getId())
                .count();
        if (count <= 0) {
            throw new CommonException("没有维修权限", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        if (correctiveMaintenance.getId() != null && correctiveMaintenance.getState() == 1) {
            //维修执行
            correctiveMaintenance.setState(2);
            correctiveMaintenance.setRepairEndTime(new Date());
            correctiveMaintenance.setRepairUserId(UserUtils.getUserId());
            correctiveMaintenance.setRepairUserName(UserUtils.getUserName());
            saveFile(correctiveMaintenance);
            correctiveMaintenanceMapper.updateById(correctiveMaintenance);
            SparePartStorageRequestDTO sparePartStorageRequestDTO = new SparePartStorageRequestDTO();
            sparePartStorageRequestDTO.setReferenceOrderNo(correctiveMaintenance.getRepairNo());
            sparePartStorageRequestDTO.setDetails(correctiveMaintenance.getDetails());
            if (CollUtil.isNotEmpty(correctiveMaintenance.getDetails())) {
                sparePartStorageRecordService.outByMaintenance(sparePartStorageRequestDTO);
            }
            if (correctiveMaintenance.getHalt() != null) {
                if (correctiveMaintenance.getHalt() == 0) {
                    deviceService.upateState(correctiveMaintenance.getCode(), UserUtils.getSite(), DeviceStateEnum.TY.getCode());
                } else {
                    deviceService.upateState(correctiveMaintenance.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
                }
            }
            final String bo = new DeviceHandleBO(UserUtils.getSite(), correctiveMaintenance.getCode()).getBo();
            repairOperateLogService.add(
                    RepairOperateLog.builder()
                            .deviceId(bo)
                            .deviceCode(correctiveMaintenance.getCode())
                            .operateType(OperateTypeEnum.UPDATE.getCode())
                            .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                            .orderTypeDesc(OrderTypeEnum.CHECK.getDesc())
                            .site(UserUtils.getSite())
                            .operateUserId(UserUtils.getUserId())
                            .operateUserName(UserUtils.getUserName())
                            .orderId(correctiveMaintenance.getId())
                            .orderNo(correctiveMaintenance.getRepairNo())
                            .build()
            );
            // correctiveMaintenanceMapper.deleteAndon(bo);
            // andon_record 表state 2修复 1异常
            String repairMethod = correctiveMaintenance.getRepairMethod();
            correctiveMaintenanceMapper.updateAndonExecute(site, correctiveMaintenance.getRepairNo(), "2", UserUtils.getUserName(), new Date(), repairMethod);
        }

        if (correctiveMaintenance.getId() != null && correctiveMaintenance.getState() == 0) {
            //受理
            correctiveMaintenance.setState(1);
            correctiveMaintenance.setRepairStartTime(new Date());
            correctiveMaintenanceMapper.updateById(correctiveMaintenance);
            deviceService.upateState(correctiveMaintenance.getCode(), UserUtils.getSite(), DeviceStateEnum.WXZ.getCode());
            repairOperateLogService.add(
                    RepairOperateLog.builder()
                            .deviceId(new DeviceHandleBO(UserUtils.getSite(), correctiveMaintenance.getCode()).getBo())
                            .deviceCode(correctiveMaintenance.getCode())
                            .operateType(OperateTypeEnum.UPDATE.getCode())
                            .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                            .orderTypeDesc(OrderTypeEnum.CHECK.getDesc())
                            .site(UserUtils.getSite())
                            .operateUserId(UserUtils.getUserId())
                            .operateUserName(UserUtils.getUserName())
                            .orderId(correctiveMaintenance.getId())
                            .orderNo(correctiveMaintenance.getRepairNo())
                            .build()
            );
            // andon_record 表state 2修复 1异常
            correctiveMaintenanceMapper.updateAndonReceive(site, correctiveMaintenance.getRepairNo(), "1", UserUtils.getUserName(), new Date());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveFile(CorrectiveMaintenance correctiveMaintenance) {
        String id = correctiveMaintenance.getId();
        LambdaQueryWrapper<MesFiles> query = new QueryWrapper<MesFiles>().lambda().eq(MesFiles::getGroupId, id).in(MesFiles::getFileType, Arrays.asList("wxhpics", "wxhvideos"));
        if (correctiveMaintenance.getMesFiles() != null && !correctiveMaintenance.getMesFiles().isEmpty()) {
            List<MesFiles> pics = correctiveMaintenance.getMesFiles().get("wxhpics");
            List<MesFiles> videos = correctiveMaintenance.getMesFiles().get("wxhvideos");
            if (pics != null && pics.size() > 0) {
                pics.forEach(file -> {
                    query.ne(MesFiles::getId, file.getId());
                    file.setGroupId(id);
                    file.setFileType("wxhpic");

                });
                mesFilesService.updateBatchById(pics);
            }
            if (videos != null && videos.size() > 0) {
                videos.forEach(file -> {
                    query.ne(MesFiles::getId, file.getId());
                    file.setGroupId(id);
                    file.setFileType("wxhvideo");

                });
                mesFilesService.updateBatchById(videos);
            }
        }
        List<MesFiles> mesFiles = mesFilesService.list(query);
        if (mesFiles != null && mesFiles.size() > 0) {
            mesFilesService.removeByIds(mesFiles.stream().map(MesFiles::getId).collect(Collectors.toList()));
        }
    }


    public CorrectiveMaintenance undoRepairById(String id) {
        CorrectiveMaintenance correctiveMaintenance = correctiveMaintenanceMapper.selectById(id);
        if (correctiveMaintenance != null && correctiveMaintenance.getState() == 0 &&
                StrUtil.equals(correctiveMaintenance.getRepairApplicantId(), UserUtils.getUserId())) {
            correctiveMaintenance.setState(3);
            correctiveMaintenanceMapper.updateById(correctiveMaintenance);
            deviceService.upateState(correctiveMaintenance.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
            repairOperateLogService.add(
                    RepairOperateLog.builder()
                            .operateType(OperateTypeEnum.CANCEL.getCode())
                            .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                            .site(UserUtils.getSite())
                            .operateUserId(UserUtils.getUserId())
                            .operateUserName(UserUtils.getUserName())
                            .orderId(correctiveMaintenance.getId())
                            .orderNo(correctiveMaintenance.getRepairNo())
                            .build()
            );
        } else {
            throw new CommonException("没有撤销权限", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        return correctiveMaintenance;
    }


    public List<AbnormalCountStatisticsDTO> queryRepairCountStatisticsByState(String site) {
        List<AbnormalCountStatisticsDTO> list = correctiveMaintenanceMapper.queryRepairCountStatisticsByState(site);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        list.forEach(e -> {
            e.setStateDesc(DeviceStateEnum.parseDescByCode(e.getState()));
        });
        return list;
    }

    /**
     * 指派工单。由指定报修人发起。判断当前用户是否在指定维修人列表，不在则抛出异常
     *
     * @param userIds       指派用户id列表
     * @param repairOrderId 维修工单id
     * @return 是否成功
     */
    public Boolean assign(List<String> userIds, String repairOrderId) {
        //判断参数
        Assert.valid(CollUtil.isEmpty(userIds), "用户id列表不能为空");
        Assert.valid(StrUtil.isBlank(repairOrderId), "工单id不能为空");
        //判断工单状态
        CorrectiveMaintenance correctiveMaintenance = correctiveMaintenanceMapper.selectById(repairOrderId);
        Assert.valid(ObjectUtil.isNull(correctiveMaintenance), "未查询到工单");
        //可指派状态列表
        List<Integer> list = ListUtil.toList(
                CorrectiveStateEnum.DWX.getCode()
        );
        Assert.valid(!CollUtil.contains(list, correctiveMaintenance.getState()), "该订单不可指派");

        //查询指派权限
        Integer count = correctiveUserService.lambdaQuery()
                .eq(CorrectiveUser::getRepairUserId, UserUtils.getUserId())
                .eq(CorrectiveUser::getRepairId, repairOrderId)
                .count();
        if (count <= 0 || !StrUtil.equals(correctiveMaintenance.getRepairApplicantId(), UserUtils.getUserId())) {
            throw new CommonException("没有指派权限", CommonExceptionDefinition.BASIC_EXCEPTION);
        }

        //增加工单指派
        ResponseData<List<IapSysUserTDto>> userListByIds = userService.getUserListByIds(userIds);
        List<IapSysUserTDto> userList = new ArrayList<>();
        if (userListByIds.isSuccess()) {
            CollUtil.addAll(userList, userListByIds.getData());
        }
        List<CorrectiveUser> correctiveUsers = new ArrayList<>();
        Map<String, String> userIdNameMap = userList.stream().collect(Collectors.toMap(IapSysUserTDto::getId, IapSysUserTDto::getUserName));
        Date now = new Date();
        //查询是否已指派
        List<CorrectiveUser> existsList = correctiveUserService.lambdaQuery().eq(CorrectiveUser::getRepairId, repairOrderId).list();
        List<String> existsRepairUserIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(existsList)) {
            existsRepairUserIds.addAll(existsList.stream().map(CorrectiveUser::getRepairUserId).collect(Collectors.toList()));
        }
        for (String userId : userIds) {
            //被指派人已是指定维修人， 跳过
            if (existsRepairUserIds.contains(userId)) {
                continue;
            }
            CorrectiveUser correctiveUser = new CorrectiveUser();
            correctiveUser.setRepairId(repairOrderId);
            correctiveUser.setRepairUserName(userIdNameMap.getOrDefault(userId, ""));
            correctiveUser.setRepairUserId(userId);
            correctiveUser.setAssignDate(now);
            correctiveUsers.add(correctiveUser);
        }
        boolean b = correctiveUserService.saveBatch(correctiveUsers);
        if (b) {
            //增加指派记录
            repairOperateLogService.add(
                    RepairOperateLog.builder()
                            .operateType(OperateTypeEnum.ASSIGN.getCode())
                            .orderType(OrderTypeEnum.CORRECTIVE.getCode())
                            .site(UserUtils.getSite())
                            .operateUserId(UserUtils.getUserId())
                            .operateUserName(UserUtils.getUserName())
                            .orderId(correctiveMaintenance.getId())
                            .orderNo(correctiveMaintenance.getRepairNo())
                            .extraData(correctiveUsers.stream().map(CorrectiveUser::getRepairUserName).collect(Collectors.joining(",")))
                            .build()
            );
        }
        return b;
    }
}










