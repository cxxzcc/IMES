package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.common.CheckStateEnum;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.common.UpkeepStateEnum;
import com.itl.iap.mes.api.dto.DataCollectionVo;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
import com.itl.iap.mes.api.dto.UpkeepExecuteQueryDto;
import com.itl.iap.mes.api.dto.UpkeepRecordDTO;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.service.RepairOperateLogService;
import com.itl.iap.mes.provider.mapper.*;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UpkeepExecuteServiceImpl {

    final private UpkeepExecuteMapper upkeepExecuteMapper;
    final private UpkeepUserMapper upkeepUserMapper;
    final private UpkeepFileMapper upkeepFileMapper;
    final private MesFilesServiceImpl mesFilesService;
    final private DeviceService deviceService;
    final private UpkeepExecuteItemMapper upkeepExecuteItemMapper;
    final private DataCollectionItemServiceImpl dataCollectionItemService;
    final private DataCollectionItemListingMapper dataCollectionItemListingMapper;
    final private RepairOperateLogService repairOperateLogService;
    final private DataCollectionMapper dataCollectionMapper;
    final private UpkeepUserServiceImpl upkeepUserService;
    final private DataCollectionServiceImpl dataCollectionService;

    final private UserService userService;
    final private DictService dictService;
    final private FileUploadServiceImpl fileUploadService;


//    public List<UpkeepExecute> findList(UpkeepExecuteQueryDto upkeepExecuteQueryDto) {
//        upkeepExecuteQueryDto.setSite(UserUtils.getSite());
//        String timeType = upkeepExecuteQueryDto.getTimeType();
//        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
//        if (dateTimeScope != null) {
//            upkeepExecuteQueryDto.setStart(LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN));
//            upkeepExecuteQueryDto.setEnd(LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
//        }
//        List<UpkeepExecute> records = upkeepExecuteMapper.findList(upkeepExecuteQueryDto);
//        if (!records.isEmpty()) {
//            //加载点检人
//            List<String> checkPlanIds = records.stream().map(UpkeepExecute::getUpkeepPlanId).collect(Collectors.toList());
//            QueryWrapper<UpkeepUser> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().in(UpkeepUser::getUpkeepId, checkPlanIds);
//            List<UpkeepUser> checkPlanUsers = upkeepUserMapper.selectList(queryWrapper);
//            Map<String, List<UpkeepUser>> planUserMap = checkPlanUsers.stream().collect(Collectors.groupingBy(UpkeepUser::getUpkeepId));
//            List<String> upkeepCodes = records.stream().map(UpkeepExecute::getCode).distinct().collect(Collectors.toList());
//            ResponseData<List<Device>> deviceVoByDevices = deviceService.getDeviceVoByDevices(upkeepCodes);
//            List<Device> data = deviceVoByDevices.getData();
//            Map<String, Device> deviceMap = new HashMap<>();
//            if (CollectionUtil.isNotEmpty(data)) {
//                deviceMap = data.stream().collect(Collectors.toMap(Device::getDevice, Function.identity()));
//            }
//            Map<String, Device> finalDeviceMap = deviceMap;
//            records.forEach(x -> {
//                        String code = x.getCode();
//                        if (finalDeviceMap.containsKey(code)) {
//                            Device deviceVo = finalDeviceMap.get(code);
//                            x.setStation(deviceVo.getStationBo());
//                            x.setDeviceModel(deviceVo.getDeviceModel());
//                        }
//                        x.setProductionLine(new ProductLineHandleBO(x.getProductionLine()).getProductLine());
//                        if (planUserMap.containsKey(x.getUpkeepPlanId())) {
//                            List<UpkeepUser> checkPlanUsers1 = planUserMap.get(x.getUpkeepPlanId());
//                            List<UpkeepUser> checkPlanUserList = new ArrayList<>();
//                            List<String> planUserIds = new ArrayList<>();
//                            List<String> planUserNames = new ArrayList<>();
//                            AtomicReference<String> operaUser = new AtomicReference<>("");
//                            AtomicReference<String> operaUserName = new AtomicReference<>("");
//                            checkPlanUsers1.stream().distinct().forEach(v -> {
//                                checkPlanUserList.add(v);
//                                planUserIds.add(v.getUpkeepUserId());
//                                planUserNames.add(v.getUpkeepUserName());
//                                if (v.getIdentity_type() != null && "0".equals(v.getIdentity_type().trim())) {
//                                    operaUser.set(v.getUpkeepUserId());
//                                    operaUserName.set(v.getUpkeepUserName());
//                                }
//                            });
//                            x.setUpkeepUserList(checkPlanUserList);
//                            x.setUpkeepUserId(String.join(",", planUserIds));
//                            x.setUpkeepUserName(String.join(",", planUserNames));
//                            x.setOperaUser(operaUser.get());
//                            x.setOperaUserName(operaUserName.get());
//                        }
//                    }
//            );
//        }
//        return records;
//    }

    public IPage<UpkeepExecute> findList(UpkeepExecuteQueryDto upkeepExecuteQueryDto, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        upkeepExecuteQueryDto.setSite(UserUtils.getSite());
        //设备相关条件
        DeviceConditionDto deviceLikeDto = new DeviceConditionDto();
        if (StrUtil.isNotBlank(upkeepExecuteQueryDto.getDiviceCode())) {
            deviceLikeDto.setDevice(upkeepExecuteQueryDto.getDiviceCode());
        }
        if (StrUtil.isNotBlank(upkeepExecuteQueryDto.getDiviceName())) {
            deviceLikeDto.setDeviceName(upkeepExecuteQueryDto.getDiviceName());
        }
        if (StrUtil.isNotBlank(upkeepExecuteQueryDto.getDiviceCodeOrName())) {
            deviceLikeDto.setDiviceCodeOrName(upkeepExecuteQueryDto.getDiviceCodeOrName());
        }
        List<DevicePlusVo> deviceList;
        if (BeanUtil.isNotEmpty(deviceLikeDto)) {
            ResponseData<List<DevicePlusVo>> dataByLike = deviceService.getDataByCondition(deviceLikeDto);
            if (dataByLike != null && ResponseData.success().getCode().equals(dataByLike.getCode())) {
                deviceList = dataByLike.getData();
                if (deviceList.size() == 0) {
                    return new Page<>();
                }
                List<String> bos = deviceList.stream().map(DevicePlusVo::getBo).collect(Collectors.toList());
                upkeepExecuteQueryDto.setDiviceCondition(bos);
            }
        }
        QueryWrapper<CheckPlan> queryWrapper = QueryWrapperUtil.convertQuery(upkeepExecuteQueryDto);
        String timeType = upkeepExecuteQueryDto.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            String start = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            String end = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            queryWrapper.apply("not (t.startTime>{0} or t.endTime < {1})", end, start);
        }
        //获取排序字段
        String sortName = upkeepExecuteQueryDto.getSortName();
        //获取排序名称
        String sortType = upkeepExecuteQueryDto.getSortType();
        IPage<UpkeepExecute> ret = upkeepExecuteMapper.findList(page, queryWrapper, sortName, sortType);
        List<UpkeepExecute> records = ret.getRecords();
        //待优化
        loadDevices(records);
        loadCheckUser(records);
        loadDataCollection(records);
        return ret;
    }

    private void loadDataCollection(List<UpkeepExecute> records) {
        //数据收集组
        List<String> dataCollectionIds = records.stream().map(UpkeepExecute::getDataCollectionId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(dataCollectionIds)) {
            List<DataCollection> dataCollections = dataCollectionMapper.selectBatchIds(dataCollectionIds);
            Map<String, DataCollection> collect = dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, Function.identity()));
            for (UpkeepExecute record : records) {
                String dataCollectionId = record.getDataCollectionId();
                if (collect.containsKey(dataCollectionId)) {
                    record.setDataCollectionName(collect.get(dataCollectionId).getName());
                }
            }
        }
    }

    private void loadDevices(List<UpkeepExecute> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            //设备状态
            ResponseData<Map<String, String>> response = dictService.getDictItemMapByParentCode("NX202012147234");
            Map<String, String> dictMap = response.getData();
            List<String> deviceIdList = records.stream().map(UpkeepExecute::getDeviceId).collect(Collectors.toList());
            //查询设备
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(deviceIdList);
            ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
            Map<String, DevicePlusVo> collect = new HashMap<>();
            if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
                List<DevicePlusVo> data = dataByCondition.getData();
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
            for (UpkeepExecute record : records) {
                String deviceId = record.getDeviceId();
                if (collect.containsKey(deviceId)) {
                    DevicePlusVo devicePlusVo = collect.get(deviceId);
                    record.setCode(devicePlusVo.getDevice());
                    record.setName(devicePlusVo.getDeviceName());
                    record.setType(devicePlusVo.getDeviceType());
                    record.setDeviceModel(devicePlusVo.getDeviceModel());
                    String stationBo = devicePlusVo.getStationBo();
                    if (StrUtil.isNotBlank(stationBo)) {
                        String[] split = stationBo.split(",");
                        if (split.length > 0) {
                            record.setStation(split[split.length - 1]);
                        }
                    }
                    String productionLine = devicePlusVo.getProductLineBo();
                    if (StrUtil.isNotBlank(productionLine)) {
                        String[] split = productionLine.split(",");
                        if (split.length > 0) {
                            record.setProductionLine(split[split.length - 1]);
                        }
                    }
                    record.setDeviceState(devicePlusVo.getState());
                    record.setDeviceStateName(dictMap.get(devicePlusVo.getState()));
                }
            }
        }
    }

    private void loadCheckUser(List<UpkeepExecute> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            List<String> idList = records.stream().map(UpkeepExecute::getUpkeepPlanId).collect(Collectors.toList());
            List<UpkeepUser> checkPlanUsers = upkeepUserService.list(new QueryWrapper<UpkeepUser>().lambda().in(UpkeepUser::getUpkeepId, idList));
            if (checkPlanUsers != null && checkPlanUsers.size() > 0) {
                Map<String, List<UpkeepUser>> map = checkPlanUsers.stream().collect(Collectors.groupingBy(UpkeepUser::getUpkeepId));
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getUpkeepPlanId())) {
                        List<UpkeepUser> upkeepUserList = map.get(x.getUpkeepPlanId());
                        for (UpkeepUser checkPlanUser : upkeepUserList) {
                            String upkeepUserId = checkPlanUser.getUpkeepUserId();
                            ResponseData<IapSysUserT> user = userService.getUser(upkeepUserId);
                            if (user != null && ResultResponseEnum.SUCCESS.getCode().equals(user.getCode())
                                    && user.getData() != null) {
                                checkPlanUser.setUpkeepUserName(user.getData().getRealName());
                            }
                        }
                        x.setUpkeepUserList(upkeepUserList);
                    }
                });
            }
        }
    }


    public UpkeepExecute findById(String id) {
        UpkeepExecute upkeepExecute = upkeepExecuteMapper.findById(id);
        String deviceId = upkeepExecute.getDeviceId();
        DeviceConditionDto deviceConditionDto = new DeviceConditionDto();
        deviceConditionDto.setBo(deviceId);
        ResponseData<List<DevicePlusVo>> deviceVoByDevice = deviceService.getDataByCondition(deviceConditionDto);
        List<DevicePlusVo> list1 = deviceVoByDevice.getData();
        DevicePlusVo data = list1.get(0);
        if (data != null) {
            //设备状态
            ResponseData<Map<String, String>> response = dictService.getDictItemMapByParentCode("NX202012147234");
            Map<String, String> dictMap = response.getData();

            upkeepExecute.setName(data.getDeviceName());
            String stationBo = data.getStationBo();
            if (StrUtil.isNotBlank(stationBo)) {
                String[] split = stationBo.split(",");
                if (split.length > 0) {
                    upkeepExecute.setStation(split[split.length - 1]);
                }
            }
            upkeepExecute.setCode(data.getDevice());
            upkeepExecute.setDeviceModel(data.getDeviceModel());
            upkeepExecute.setType(data.getDeviceType());
            String productionLine = data.getProductLineBo();
            if (StrUtil.isNotBlank(productionLine)) {
                String[] split = productionLine.split(",");
                if (split.length > 0) {
                    upkeepExecute.setProductionLine(split[split.length - 1]);
                }
            }
            upkeepExecute.setDeviceState(data.getState());
            upkeepExecute.setDeviceStateName(dictMap.get(data.getState()));
        }
        //  待优化 看不懂的代码
        List<UpkeepExecuteItem> select = upkeepExecuteItemMapper.selectList(new QueryWrapper<UpkeepExecuteItem>().eq("upkeepExecuteId", id));
        if (CollectionUtil.isNotEmpty(select)) {
            upkeepExecute.setUpkeepExecuteItemList(select);
            for (UpkeepExecuteItem upkeepExecuteItem : select) {
                if (StrUtil.isNotBlank(upkeepExecuteItem.getDataCollectionItemId())) {
                    QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dataCollectionItemId", upkeepExecuteItem.getDataCollectionItemId());
                    List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                    upkeepExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                }
                if (StrUtil.isNotBlank(upkeepExecuteItem.getImgSrc())) {
                    ResponseData<List<MesFiles>> fileList = fileUploadService.getFileList(upkeepExecuteItem.getDataCollectionItemId());
                    if (fileList != null && ResultResponseEnum.SUCCESS.getCode().equals(fileList.getCode())) {
                        List<MesFiles> data1 = fileList.getData();
                        if (CollectionUtil.isNotEmpty(data1)) {
                            List<MesFilesVO> mesFilesVOs;
                            if (Boolean.FALSE.equals(CollectionUtils.isEmpty(data1))) {
                                mesFilesVOs = data1.stream().map(x -> {
                                    MesFilesVO mesFilesVO = new MesFilesVO();
                                    BeanUtils.copyProperties(x, mesFilesVO);
                                    return mesFilesVO;
                                }).collect(Collectors.toList());
                                upkeepExecuteItem.setMesFiles(mesFilesVOs);
                            }
                        }
                    }
                }
            }
        } else {
            List<UpkeepExecuteItem> upkeepExecuteItems = new ArrayList<>();
            IPage<DataCollectionItem> list = dataCollectionItemService.findList(upkeepExecute.getDataCollectionId(), 1, 1000);
            List<DataCollectionItem> records = list.getRecords();
            if (records != null && !records.isEmpty()) {
                records.forEach(dataCollectionItem -> {
                    UpkeepExecuteItem upkeepExecuteItem = new UpkeepExecuteItem();
                    upkeepExecuteItem.setItemCode(dataCollectionItem.getItemNo());
                    upkeepExecuteItem.setItemName(dataCollectionItem.getItemName());
                    upkeepExecuteItem.setType(dataCollectionItem.getItemType());
                    upkeepExecuteItem.setMaxNum(dataCollectionItem.getMaxNum());
                    upkeepExecuteItem.setMinNum(dataCollectionItem.getMinNum());
                    upkeepExecuteItem.setRemark(dataCollectionItem.getRemark());
                    if (StrUtil.isNotBlank(dataCollectionItem.getImgSrc())) {
                        ResponseData<List<MesFiles>> fileList = fileUploadService.getFileList(dataCollectionItem.getId());
                        if (fileList != null && ResultResponseEnum.SUCCESS.getCode().equals(fileList.getCode())) {
                            List<MesFiles> data1 = fileList.getData();
                            if (CollectionUtil.isNotEmpty(data1)) {
                                List<MesFilesVO> mesFilesVOs;
                                if (Boolean.FALSE.equals(CollectionUtils.isEmpty(data1))) {
                                    mesFilesVOs = data1.stream().map(x -> {
                                        MesFilesVO mesFilesVO = new MesFilesVO();
                                        BeanUtils.copyProperties(x, mesFilesVO);
                                        return mesFilesVO;
                                    }).collect(Collectors.toList());
                                    upkeepExecuteItem.setMesFiles(mesFilesVOs);
                                    upkeepExecuteItem.setImgSrc("img");
                                }
                            }
                        }
                    }
                    QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dataCollectionItemId", dataCollectionItem.getId());
                    List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                    upkeepExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                    upkeepExecuteItems.add(upkeepExecuteItem);
                });
                upkeepExecute.setUpkeepExecuteItemList(upkeepExecuteItems);
            }
        }
        //保养人
        QueryWrapper<UpkeepUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UpkeepUser::getUpkeepId, upkeepExecute.getUpkeepPlanId());
        List<UpkeepUser> upkeepUsers = upkeepUserMapper.selectList(queryWrapper);
        for (UpkeepUser upkeepUser : upkeepUsers) {
            String checkUserId = upkeepUser.getUpkeepUserId();
            String identityType = upkeepUser.getIdentity_type();
            ResponseData<IapSysUserT> user = userService.getUser(checkUserId);
            IapSysUserT data1 = user.getData();
            if (data1 != null) {
                upkeepUser.setUpkeepUserName(data1.getRealName());
                if (identityType != null && "0".equals(identityType.trim())) {
                    upkeepExecute.setOperaUser(checkUserId);
                    upkeepExecute.setOperaUserName(data1.getRealName());
                }
            }
        }
        upkeepExecute.setUpkeepUserList(upkeepUsers);
        //数据收集组
        DataCollectionVo dataCollectionVo = dataCollectionMapper.getById(upkeepExecute.getDataCollectionId());
        if (dataCollectionVo != null) {
            upkeepExecute.setDataCollectionName(dataCollectionVo.getName());
        }
        //文件列表
        List<UpkeepFile> fileList = upkeepFileMapper.selectList(Wrappers.<UpkeepFile>lambdaQuery().eq(UpkeepFile::getUpkeepExecuteId, id));
        List<String> idList = fileList.stream().map(UpkeepFile::getFileId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<MesFiles> mesFiles = mesFilesService.list(Wrappers.<MesFiles>lambdaQuery().in(MesFiles::getId, idList));
            upkeepExecute.setMesFiles(mesFiles);
        }
        return upkeepExecute;
    }


    @Transactional
    public void save(UpkeepExecute upkeepExecute) throws CommonException {
        //保养人才可以保养
        String id = upkeepExecute.getId();
        String upkeepPlanId = upkeepExecute.getUpkeepPlanId();
        QueryWrapper<UpkeepUser> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(UpkeepUser::getUpkeepId, upkeepPlanId);
        List<UpkeepUser> upkeepUserList = upkeepUserMapper.selectList(queryWrapper2);
        Map<String, UpkeepUser> collect = upkeepUserList.stream().collect(Collectors.toMap(UpkeepUser::getUpkeepUserId, Function.identity()));
        UserTDto currentUser = UserUtils.getCurrentUser();
        if (currentUser != null && !collect.containsKey(currentUser.getUserName())) {
            throw new CommonException("非点检人不能点检", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //如果此时设备状态不是可用的禁止受理给出提示
        String deviceId = upkeepExecute.getDeviceId();
        DeviceConditionDto deviceConditionDto = new DeviceConditionDto();
        deviceConditionDto.setBo(deviceId);
        ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(deviceConditionDto);
        DevicePlusVo devicePlusVo = new DevicePlusVo();
        if (dataByCondition != null && ResultResponseEnum.SUCCESS.getCode().equals(dataByCondition.getCode())
                && dataByCondition.getData() != null) {
            devicePlusVo = dataByCondition.getData().get(0);
        }
        UpkeepExecute upkeepExecute1 = upkeepExecuteMapper.selectById(id);
        if (upkeepExecute1 != null && upkeepExecute1.getStartTime() == null) {
            upkeepExecute.setStartTime(new Date());
        }
        String deviceState = devicePlusVo.getState();
        if (UpkeepStateEnum.DBY.getCode().equals(upkeepExecute1.getState())) {
            if (!DeviceStateEnum.ZY.getCode().equals(deviceState)) {
                String bo = devicePlusVo.getBo();
                //获取设备最新操作记录
                DeviceStateEnum deviceStateEnum = Arrays.stream(DeviceStateEnum.values()).filter(v -> v.getCode().equals(deviceState)).findFirst().get();
                RepairOperateLog operateLog = repairOperateLogService.getListByDeviceId(bo);
                String msg = "当前设备" + deviceStateEnum.getDesc() + "。";
                if (operateLog != null) {
                    String operateUserName = operateLog.getOperateUserName() == null ? "" : operateLog.getOperateUserName();
                    String orderTypeDesc = operateLog.getOrderTypeDesc();
                    String orderNo = operateLog.getOrderNo();
                    msg += "【姓名:" + operateUserName + ",操作:" + orderTypeDesc + ",工单:" + orderNo + "】";
                }
                msg += "，请稍后再试。";
                throw new CommonException(msg, CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
        upkeepExecute.setSite(UserUtils.getSite());
        List<UpkeepExecuteItem> upkeepExecuteItemList = upkeepExecute.getUpkeepExecuteItemList();

        String operateType;
        Integer state = upkeepExecute.getState();
        if (1 == state) {//保养完成需要更改状态
            upkeepExecute.setEndTime(new Date());
            deviceService.upateState(upkeepExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
            operateType = OperateTypeEnum.EXECUTE.getCode();
        } else {
            deviceService.upateState(upkeepExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.BYZ.getCode());
            operateType = OperateTypeEnum.ACCEPT.getCode();
        }
        String operaUser = upkeepExecute.getOperaUser();
        if (StrUtil.isBlank(operaUser) && currentUser != null) {
            operaUser = currentUser.getUserName();
        }
        if (StrUtil.isBlank(operaUser) && UserUtils.getCurrentUser() != null) {
            //清除操作人
            QueryWrapper<UpkeepUser> queryWrapper1 = new QueryWrapper();
            queryWrapper1.lambda().eq(UpkeepUser::getUpkeepId, upkeepPlanId);
            UpkeepUser planUser1 = new UpkeepUser();
            planUser1.setUpkeepId(upkeepPlanId);
            planUser1.setIdentity_type(null);
            upkeepUserMapper.update(planUser1, queryWrapper1);
            //操作人
            QueryWrapper<UpkeepUser> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(UpkeepUser::getUpkeepId, upkeepPlanId).eq(UpkeepUser::getUpkeepUserId, operaUser);
            UpkeepUser planUser = new UpkeepUser();
            planUser.setUpkeepId(upkeepPlanId);
            planUser.setUpkeepUserId(operaUser);
            planUser.setIdentity_type("0");
            upkeepUserMapper.update(planUser, queryWrapper);
        }
        QueryWrapper<UpkeepExecuteItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("upkeepExecuteId", id);
        upkeepExecuteItemMapper.delete(queryWrapper);
        if (upkeepExecuteItemList != null && !upkeepExecuteItemList.isEmpty()) {
            upkeepExecuteItemList.forEach(upkeepExecuteItem -> {
                if (1 == state) {//提交
                    Integer maxNum = upkeepExecuteItem.getMaxNum();
                    Integer minNum = upkeepExecuteItem.getMinNum();
                    if (maxNum > 0 && minNum > 0) {
                        Integer itemValue = Integer.valueOf(upkeepExecuteItem.getItemValue());
                        if (itemValue < minNum || itemValue > maxNum) {
                            //异常
                            upkeepExecute.setState(CheckStateEnum.YC.getCode());
                        }
                    }
                }
                upkeepExecuteItem.setId(null);
                upkeepExecuteItem.setUpkeepExecuteId(upkeepExecute.getId());
                if (CollectionUtil.isNotEmpty(upkeepExecuteItem.getDataCollectionItemListingList())) {
                    upkeepExecuteItem.setDataCollectionItemId(upkeepExecuteItem.getDataCollectionItemListingList().get(0).getDataCollectionItemId());
                }
                saveItem(upkeepExecuteItem);
            });
        }
        upkeepExecuteMapper.updateById(upkeepExecute);
        //保存文件列表
        List<MesFiles> mesFiles = upkeepExecute.getMesFiles();
        upkeepFileMapper.delete(Wrappers.<UpkeepFile>lambdaQuery().eq(UpkeepFile::getUpkeepExecuteId, upkeepExecute.getId()));
        if (CollectionUtil.isNotEmpty(mesFiles)) {
            List<UpkeepFile> fileList = new ArrayList<>();
            for (MesFiles mesFile : mesFiles) {
                UpkeepFile upkeepFile = new UpkeepFile();
                upkeepFile.setFileId(mesFile.getId());
                upkeepFile.setUpkeepExecuteId(upkeepExecute.getId());
                fileList.add(upkeepFile);
            }
            upkeepFileMapper.insertBatch(fileList);
        }
        repairOperateLogService.add(
                RepairOperateLog.builder()
                        .deviceId(devicePlusVo.getBo())
                        .deviceCode(devicePlusVo.getDevice())
                        .operateType(operateType)
                        .orderType(OrderTypeEnum.UPKEEP.getCode())
                        .orderTypeDesc(OrderTypeEnum.UPKEEP.getDesc())
                        .site(UserUtils.getSite())
                        .operateUserId(UserUtils.getUserId())
                        .operateUserName(UserUtils.getUserName())
                        .orderId(upkeepExecute.getId())
                        .orderNo(upkeepExecute.getUpkeepNo())
                        .build()
        );
    }

    public void saveItem(UpkeepExecuteItem upkeepExecuteItem) {
        if (upkeepExecuteItem.getId() != null) {
            upkeepExecuteItemMapper.updateById(upkeepExecuteItem);
        } else {
            upkeepExecuteItemMapper.insert(upkeepExecuteItem);
        }
    }

    /**
     * 设备保养记录
     *
     * @param params code=设备编号；分页参数
     * @return 保养记录列表
     */
    public Page<UpkeepRecordDTO> upkeepRecord(Map<String, Object> params) {
        Page<UpkeepRecordDTO> queryPage = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        params.put("deviceId", new DeviceHandleBO(UserUtils.getSite(), (String) params.get("code")).getBo());
        List<UpkeepRecordDTO> list = upkeepExecuteMapper.upkeepRecord(queryPage, params);
        //获取数据收集组
        List<String> dataCollectionIds = list.stream().map(UpkeepRecordDTO::getDataCollectionId).distinct().collect(Collectors.toList());
        List<DataCollection> dataCollections = dataCollectionService.findByIds(dataCollectionIds);
        Map<String, String> dataCollectIdNameMap = new HashMap<>(16);
        if (CollUtil.isNotEmpty(dataCollections)) {
            dataCollectIdNameMap.putAll(dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, e -> StrUtil.isBlank(e.getName()) ? "" : e.getName())));
        }
        list.forEach(e -> {
            if (dataCollectIdNameMap.containsKey(e.getDataCollectionId())) {
                e.setDataCollectionName(dataCollectIdNameMap.get(e.getDataCollectionId()));
            }
        });
        queryPage.setRecords(list);
        return queryPage;
    }

    /**
     * 获取待保养的工单数
     *
     * @param site   工厂id
     * @param userId 用户id
     * @return
     */
    public Integer getUpkeepCountByUser(String userId, String site) {
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(site)) {
            return 0;
        }
        return upkeepExecuteMapper.getUpkeepCountByUser(userId, site);
    }

    public void updateUpkeepExecuteState(List<String> code, Integer state) {
        UpkeepExecute upkeepExecute = new UpkeepExecute();
        upkeepExecute.setState(state);
        UpdateWrapper<UpkeepExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(UpkeepExecute::getCode, code).eq(UpkeepExecute::getState, UpkeepStateEnum.DBY.getCode());
        upkeepExecuteMapper.update(upkeepExecute, updateWrapper);
    }

    /**
     * 点检工单统计
     *
     * @param params 查询参数
     */
    public List<RepairExecuteCountStatisticsDTO> checkExecuteStatistics(Map<String, Object> params) {
        return upkeepExecuteMapper.checkExecuteStatistics(params);
    }

    public void updateCheckExecuteStateByDeviceId(String planId, List<String> deviceIds, String user, Integer state) {
        UpkeepExecute upkeepExecute = new UpkeepExecute();
        upkeepExecute.setState(state);
        upkeepExecute.setUpdateBy(StrUtil.isBlank(user) ? "admin" : user);
        UpdateWrapper<UpkeepExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(UpkeepExecute::getDeviceId, deviceIds)
                .eq(UpkeepExecute::getUpkeepPlanId, planId)
                .eq(UpkeepExecute::getState, UpkeepStateEnum.DBY.getCode());
        upkeepExecuteMapper.update(upkeepExecute, updateWrapper);
    }
}
