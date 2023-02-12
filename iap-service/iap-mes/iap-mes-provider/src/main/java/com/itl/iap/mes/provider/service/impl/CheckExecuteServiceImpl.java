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
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.common.CheckStateEnum;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import com.itl.iap.mes.api.dto.CheckExecuteQueryDto;
import com.itl.iap.mes.api.dto.CheckRecordDTO;
import com.itl.iap.mes.api.dto.DataCollectionVo;
import com.itl.iap.mes.api.dto.RepairExecuteCountStatisticsDTO;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CheckExecuteServiceImpl {

    final private CheckExecuteMapper checkExecuteMapper;
    final private CheckExecuteItemMapper checkExecuteItemMapper;
    final private CheckPlanUserMapper checkPlanUserMapper;
    final private MesFilesServiceImpl mesFilesService;
    final private DeviceService deviceService;
    final private CheckFileMapper checkFileMapper;
    final private DataCollectionItemServiceImpl dataCollectionItemService;
    final private DataCollectionItemListingMapper dataCollectionItemListingMapper;
    final private RepairOperateLogService repairOperateLogService;
    final private DataCollectionMapper dataCollectionMapper;
    final private CheckPlanUserServiceImpl checkPlanUserService;
    final private UserService userService;
    final private DictService dictService;
    final private DataCollectionServiceImpl dataCollectionService;
    final private FileUploadServiceImpl fileUploadService;


//    public List<CheckExecute> findList(CheckExecuteQueryDto checkExecute) {
//        checkExecute.setSiteId(UserUtils.getSite());
//        String timeType = checkExecute.getTimeType();
//        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
//        if (dateTimeScope != null) {
//            checkExecute.setStart(LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN));
//            checkExecute.setEnd(LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN));
//        }
//        List<CheckExecute> records = checkExecuteMapper.listQuery(checkExecute);
//        if (!records.isEmpty()) {
//            //加载点检人
//            List<String> checkPlanIds = records.stream().map(CheckExecute::getCheckPlanId).collect(Collectors.toList());
//            QueryWrapper<CheckPlanUser> queryWrapper = new QueryWrapper<>();
//            queryWrapper.lambda().in(CheckPlanUser::getCheckId, checkPlanIds);
//            List<CheckPlanUser> checkPlanUsers = checkPlanUserMapper.selectList(queryWrapper);
//            Map<String, List<CheckPlanUser>> planUserMap = checkPlanUsers.stream().collect(Collectors.groupingBy(CheckPlanUser::getCheckId));
//            records.forEach(x -> {
//                        x.setProductionLine(new ProductLineHandleBO(x.getProductionLine()).getProductLine());
//                        if (planUserMap.containsKey(x.getCheckPlanId())) {
//                            List<CheckPlanUser> checkPlanUsers1 = planUserMap.get(x.getCheckPlanId());
//                            List<CheckPlanUser> checkPlanUserList = new ArrayList<>();
//                            List<String> planUserIds = new ArrayList<>();
//                            List<String> planUserNames = new ArrayList<>();
//                            AtomicReference<String> operaUser = new AtomicReference<>("");
//                            AtomicReference<String> operaUserName = new AtomicReference<>("");
//                            checkPlanUsers1.stream().distinct().forEach(v -> {
//                                checkPlanUserList.add(v);
//                                planUserIds.add(v.getCheckUserId());
//                                planUserNames.add(v.getCheckUserName());
//                                if (v.getIdentityType() != null && "0".equals(v.getIdentityType().trim())) {
//                                    operaUser.set(v.getCheckUserId());
//                                    operaUserName.set(v.getCheckUserName());
//                                }
//                            });
//                            x.setCheckPlanUserList(checkPlanUserList);
////                            x.setCheckUserId(String.join(",", planUserIds));
////                            x.setCheckUserName(String.join(",", planUserNames));
//                            x.setOperaUser(operaUser.get());
//                            x.setOperaUserName(operaUserName.get());
//                        }
//                    }
//            );
//        }
//        return records;
//    }


    public IPage<CheckExecute> findList(CheckExecuteQueryDto checkExecuteQueryDto, Integer pageNum, Integer pageSize) {
        checkExecuteQueryDto.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum, pageSize);
        //设备相关条件
        DeviceConditionDto deviceLikeDto = new DeviceConditionDto();
        if (StrUtil.isNotBlank(checkExecuteQueryDto.getDiviceCode())) {
            deviceLikeDto.setDevice(checkExecuteQueryDto.getDiviceCode());
        }
        if (StrUtil.isNotBlank(checkExecuteQueryDto.getDiviceName())) {
            deviceLikeDto.setDeviceName(checkExecuteQueryDto.getDiviceName());
        }
        if (StrUtil.isNotBlank(checkExecuteQueryDto.getDiviceCodeOrName())) {
            deviceLikeDto.setDiviceCodeOrName(checkExecuteQueryDto.getDiviceCodeOrName());
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
                checkExecuteQueryDto.setDiviceCondition(bos);
            }
        }
        QueryWrapper<CheckPlan> queryWrapper = QueryWrapperUtil.convertQuery(checkExecuteQueryDto);
        String timeType = checkExecuteQueryDto.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            String start = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            String end = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            queryWrapper.apply("not (startTime>{0} or endTime < {1})", end, start);
        }
        IPage<CheckExecute> ret = checkExecuteMapper.pageQuery(page, queryWrapper);
        List<CheckExecute> records = ret.getRecords();
        //待优化
        loadDevices(records);
        loadCheckUser(records);
        loadDataCollection(records);
        return ret;
    }


    private void loadDataCollection(List<CheckExecute> records) {
        //数据收集组
        List<String> dataCollectionIds = records.stream().map(CheckExecute::getDataCollectionId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(dataCollectionIds)) {
            List<DataCollection> dataCollections = dataCollectionMapper.selectBatchIds(dataCollectionIds);
            Map<String, DataCollection> collect = dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, Function.identity()));
            for (CheckExecute record : records) {
                String dataCollectionId = record.getDataCollectionId();
                if (collect.containsKey(dataCollectionId)) {
                    record.setDataCollectionName(collect.get(dataCollectionId).getName());
                }
            }
        }
    }

    private void loadDevices(List<CheckExecute> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            //设备状态
            ResponseData<Map<String, String>> response = dictService.getDictItemMapByParentCode("NX202012147234");
            Map<String, String> dictMap = response.getData();
            List<String> deviceIdList = records.stream().map(CheckExecute::getDeviceId).collect(Collectors.toList());
            //查询设备
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(deviceIdList);
            ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
            Map<String, DevicePlusVo> collect = new HashMap<>();
            if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
                List<DevicePlusVo> data = dataByCondition.getData();
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
            for (CheckExecute record : records) {
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

    private void loadCheckUser(List<CheckExecute> records) {
        if (CollectionUtil.isNotEmpty(records)) {
            List<String> idList = records.stream().map(CheckExecute::getCheckPlanId).collect(Collectors.toList());
            List<CheckPlanUser> checkPlanUsers = checkPlanUserService.list(new QueryWrapper<CheckPlanUser>().lambda().in(CheckPlanUser::getCheckId, idList));
            if (checkPlanUsers != null && checkPlanUsers.size() > 0) {
                Map<String, List<CheckPlanUser>> map = checkPlanUsers.stream().collect(Collectors.groupingBy(CheckPlanUser::getCheckId));
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getCheckPlanId())) {
                        List<CheckPlanUser> checkPlanUserList = map.get(x.getCheckPlanId());
                        for (CheckPlanUser checkPlanUser : checkPlanUserList) {
                            String checkUserId = checkPlanUser.getCheckUserId();
                            ResponseData<IapSysUserT> user = userService.getUser(checkUserId);
                            if (user != null && ResultResponseEnum.SUCCESS.getCode().equals(user.getCode())
                                    && user.getData() != null) {
                                checkPlanUser.setCheckUserName(user.getData().getRealName());
                            }
                        }
                        x.setCheckPlanUserList(checkPlanUserList);
                    }
                });
            }
        }
    }

    public CheckExecute findById(String id) throws CommonException {
        CheckExecute checkExecute = checkExecuteMapper.findById(id);
        String deviceId = checkExecute.getDeviceId();
        DeviceConditionDto deviceConditionDto = new DeviceConditionDto();
        deviceConditionDto.setBo(deviceId);
        ResponseData<List<DevicePlusVo>> deviceVoByDevice = deviceService.getDataByCondition(deviceConditionDto);
        List<DevicePlusVo> list1 = deviceVoByDevice.getData();
        DevicePlusVo data = list1.get(0);
        if (data != null) {
            //设备状态
            ResponseData<Map<String, String>> response = dictService.getDictItemMapByParentCode("NX202012147234");
            Map<String, String> dictMap = response.getData();

            checkExecute.setName(data.getDeviceName());
            String stationBo = data.getStationBo();
            if (StrUtil.isNotBlank(stationBo)) {
                String[] split = stationBo.split(",");
                if (split.length > 0) {
                    checkExecute.setStation(split[split.length - 1]);
                }
            }
            checkExecute.setCode(data.getDevice());
            checkExecute.setDeviceModel(data.getDeviceModel());
            checkExecute.setType(data.getDeviceType());
            String productionLine = data.getProductLineBo();
            if (StrUtil.isNotBlank(productionLine)) {
                String[] split = productionLine.split(",");
                if (split.length > 0) {
                    checkExecute.setProductionLine(split[split.length - 1]);
                }
            }
            checkExecute.setDeviceState(data.getState());
            checkExecute.setDeviceStateName(dictMap.get(data.getState()));
        }

        List<CheckExecuteItem> select = checkExecuteItemMapper.selectList(new QueryWrapper<CheckExecuteItem>().eq("checkExecuteId", id));
        //待优化
        if (select != null && !select.isEmpty()) {
            checkExecute.setCheckExecuteItemList(select);
            for (CheckExecuteItem checkExecuteItem : select) {
                if (StrUtil.isNotBlank(checkExecuteItem.getDataCollectionItemId())) {
                    QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dataCollectionItemId", checkExecuteItem.getDataCollectionItemId());
                    List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                    checkExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                }
                if (StrUtil.isNotBlank(checkExecuteItem.getImgSrc())) {
                    ResponseData<List<MesFiles>> fileList = fileUploadService.getFileList(checkExecuteItem.getDataCollectionItemId());
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
                                checkExecuteItem.setMesFiles(mesFilesVOs);
                            }
                        }
                    }
                }
            }
        } else {
            List<CheckExecuteItem> checkExecuteItems = new ArrayList<>();
            IPage<DataCollectionItem> list = dataCollectionItemService.findList(checkExecute.getDataCollectionId(), 1, 1000);
            List<DataCollectionItem> records = list.getRecords();
            if (records != null && !records.isEmpty()) {
                records.forEach(dataCollectionItem -> {
                    CheckExecuteItem checkExecuteItem = new CheckExecuteItem();
                    checkExecuteItem.setItemCode(dataCollectionItem.getItemNo());
                    checkExecuteItem.setItemName(dataCollectionItem.getItemName());
                    checkExecuteItem.setType(dataCollectionItem.getItemType());
                    checkExecuteItem.setMaxNum(dataCollectionItem.getMaxNum());
                    checkExecuteItem.setMinNum(dataCollectionItem.getMinNum());
                    checkExecuteItem.setRemark(dataCollectionItem.getRemark());
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
                                    checkExecuteItem.setMesFiles(mesFilesVOs);
                                    checkExecuteItem.setImgSrc("img");
                                }
                            }
                        }
                    }
                    QueryWrapper<DataCollectionItemListing> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("dataCollectionItemId", dataCollectionItem.getId());
                    List<DataCollectionItemListing> dataCollectionItemListingList = dataCollectionItemListingMapper.selectList(queryWrapper);
                    checkExecuteItem.setDataCollectionItemListingList(dataCollectionItemListingList);
                    checkExecuteItems.add(checkExecuteItem);
                });
                checkExecute.setCheckExecuteItemList(checkExecuteItems);
            }
        }
        //点检人
        QueryWrapper<CheckPlanUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(CheckPlanUser::getCheckId, checkExecute.getCheckPlanId());
        List<CheckPlanUser> upkeepUsers = checkPlanUserMapper.selectList(queryWrapper);
        for (CheckPlanUser checkPlanUser : upkeepUsers) {
            String checkUserId = checkPlanUser.getCheckUserId();
            String identityType = checkPlanUser.getIdentityType();
            ResponseData<IapSysUserT> user = userService.getUser(checkUserId);
            IapSysUserT data1 = user.getData();
            if (data1 != null) {
                checkPlanUser.setCheckUserName(data1.getRealName());
                if (identityType != null && "0".equals(identityType.trim())) {
                    checkExecute.setOperaUser(checkUserId);
                    checkExecute.setOperaUserName(data1.getRealName());
                }
            }
        }
        checkExecute.setCheckPlanUserList(upkeepUsers);
        //数据收集组
        DataCollectionVo dataCollectionVo = dataCollectionMapper.getById(checkExecute.getDataCollectionId());
        if (dataCollectionVo != null) {
            checkExecute.setDataCollectionName(dataCollectionVo.getName());
        }
        //文件列表
        List<CheckFile> fileList = checkFileMapper.selectList(Wrappers.<CheckFile>lambdaQuery().eq(CheckFile::getCheckExecuteId, id));
        List<String> idList = fileList.stream().map(CheckFile::getFileId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<MesFiles> mesFiles = mesFilesService.list(Wrappers.<MesFiles>lambdaQuery().in(MesFiles::getId, idList));
            checkExecute.setMesFiles(mesFiles);
        }
        return checkExecute;
    }


    @Transactional
    public void save(CheckExecute checkExecute) {
        //点检人才可以点检
        String id = checkExecute.getId();
        String checkPlanId = checkExecute.getCheckPlanId();
        QueryWrapper<CheckPlanUser> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(CheckPlanUser::getCheckId, checkPlanId);
        List<CheckPlanUser> checkPlanUserList = checkPlanUserMapper.selectList(queryWrapper2);
        Map<String, CheckPlanUser> collect = checkPlanUserList.stream().collect(Collectors.toMap(CheckPlanUser::getCheckUserId, Function.identity()));
        UserTDto currentUser = UserUtils.getCurrentUser();
        if (currentUser != null && !collect.containsKey(currentUser.getUserName())) {
            throw new CommonException("非点检人不能点检", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //如果此时设备状态不是可用的禁止受理给出提示
        String deviceId = checkExecute.getDeviceId();
        DeviceConditionDto deviceConditionDto = new DeviceConditionDto();
        deviceConditionDto.setBo(deviceId);
        ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(deviceConditionDto);
        DevicePlusVo devicePlusVo = new DevicePlusVo();
        if (dataByCondition != null && ResultResponseEnum.SUCCESS.getCode().equals(dataByCondition.getCode())
                && dataByCondition.getData() != null) {
            devicePlusVo = dataByCondition.getData().get(0);
        }
        CheckExecute checkExecute1 = checkExecuteMapper.selectById(id);
        if (checkExecute1 != null && checkExecute1.getStartTime() == null) {
            checkExecute.setStartTime(new Date());
        }
        if (CheckStateEnum.DDZ.getCode().equals(checkExecute1.getState())) {
            String deviceState = devicePlusVo.getState();
            if (!DeviceStateEnum.ZY.getCode().equals(deviceState)) {
                String bo = devicePlusVo.getBo();
                //获取设备最新操作记录
                DeviceStateEnum deviceStateEnum = Arrays.stream(DeviceStateEnum.values()).filter(v -> v.getCode().equals(deviceState)).findFirst().get();
                String msg = "当前设备" + deviceStateEnum.getDesc() + "。";
                RepairOperateLog operateLog = repairOperateLogService.getListByDeviceId(bo);
                if (operateLog != null) {
                    String operateUserName = operateLog.getOperateUserName() == null ? "" : operateLog.getOperateUserName();
                    String orderTypeDesc = operateLog.getOrderTypeDesc();
                    String orderNo = operateLog.getOrderNo();
                    msg += "【姓名:" + operateUserName + ", 操作:" + orderTypeDesc + ", 工单:" + orderNo + "】";
                }
                msg += "，请稍后再试。";
                throw new CommonException(msg, CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }

        checkExecute.setSiteId(UserUtils.getSite());
        List<CheckExecuteItem> checkExecuteItemList = checkExecute.getCheckExecuteItemList();

        Integer state = checkExecute.getState();
        String operateType;
        if (1 == state) {//点检完成需要更改状态
            checkExecute.setEndTime(new Date());
            deviceService.upateState(checkExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.ZY.getCode());
            operateType = OperateTypeEnum.EXECUTE.getCode();
        } else {
            deviceService.upateState(checkExecute.getCode(), UserUtils.getSite(), DeviceStateEnum.DJZ.getCode());
            operateType = OperateTypeEnum.ACCEPT.getCode();
        }
        String operaUser = checkExecute.getOperaUser();
        if (StrUtil.isBlank(operaUser) && currentUser != null) {
            operaUser = currentUser.getUserName();
        }
        if (StrUtil.isNotBlank(operaUser) && StrUtil.isNotBlank(checkPlanId)) {
            //清除操作人
            QueryWrapper<CheckPlanUser> queryWrapper1 = new QueryWrapper();
            queryWrapper1.lambda().eq(CheckPlanUser::getCheckId, checkPlanId);
            CheckPlanUser planUser1 = new CheckPlanUser();
            planUser1.setCheckId(checkPlanId);
            planUser1.setIdentityType(null);
            checkPlanUserMapper.update(planUser1, queryWrapper1);
            //操作人
            QueryWrapper<CheckPlanUser> queryWrapper = new QueryWrapper();
            queryWrapper.lambda().eq(CheckPlanUser::getCheckId, checkPlanId).eq(CheckPlanUser::getCheckUserId, operaUser);
            CheckPlanUser planUser = new CheckPlanUser();
            planUser.setCheckId(checkPlanId);
            planUser.setCheckUserId(operaUser);
            planUser.setIdentityType("0");
            checkPlanUserMapper.update(planUser, queryWrapper);
        }
        QueryWrapper<CheckExecuteItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("checkExecuteId", id);
        checkExecuteItemMapper.delete(queryWrapper);
        if (checkExecuteItemList != null && !checkExecuteItemList.isEmpty()) {
            checkExecuteItemList.forEach(checkExecuteItem -> {
                if (1 == state) {//提交
                    Integer maxNum = checkExecuteItem.getMaxNum();
                    Integer minNum = checkExecuteItem.getMinNum();
                    if (maxNum > 0 && minNum > 0) {
                        Integer itemValue = Integer.valueOf(checkExecuteItem.getItemValue());
                        if (itemValue < minNum || itemValue > maxNum) {
                            //异常
                            checkExecute.setState(CheckStateEnum.YC.getCode());
                        }
                    }
                }
                checkExecuteItem.setId(null);
                checkExecuteItem.setCheckExecuteId(id);
                if (CollectionUtil.isNotEmpty(checkExecuteItem.getDataCollectionItemListingList())) {
                    checkExecuteItem.setDataCollectionItemId(checkExecuteItem.getDataCollectionItemListingList().get(0).getDataCollectionItemId());
                }
                saveItem(checkExecuteItem);
            });
        }
        checkExecuteMapper.updateById(checkExecute);
        //保存文件列表
        List<MesFiles> mesFiles = checkExecute.getMesFiles();
        checkFileMapper.delete(Wrappers.<CheckFile>lambdaQuery().eq(CheckFile::getCheckExecuteId, id));
        if (CollectionUtil.isNotEmpty(mesFiles)) {
            List<CheckFile> fileList = new ArrayList<>();
            for (MesFiles mesFile : mesFiles) {
                CheckFile checkFile = new CheckFile();
                checkFile.setFileId(mesFile.getId());
                checkFile.setCheckExecuteId(id);
                fileList.add(checkFile);
            }
            checkFileMapper.insertBatch(fileList);
        }
        repairOperateLogService.add(
                RepairOperateLog.builder()
                        .deviceId(devicePlusVo.getBo())
                        .deviceCode(devicePlusVo.getDevice())
                        .operateType(operateType)
                        .orderType(OrderTypeEnum.CHECK.getCode())
                        .orderTypeDesc(OrderTypeEnum.CHECK.getDesc())
                        .site(UserUtils.getSite())
                        .operateUserId(UserUtils.getUserId())
                        .operateUserName(UserUtils.getUserName())
                        .orderId(checkExecute.getId())
                        .orderNo(checkExecute.getCheckNo())
                        .build()
        );
    }

    public void saveItem(CheckExecuteItem checkExecuteItem) {
        if (checkExecuteItem.getId() != null) {
            checkExecuteItemMapper.updateById(checkExecuteItem);
        } else {
            checkExecuteItemMapper.insert(checkExecuteItem);
        }
    }

    /**
     * 点检保养记录
     *
     * @param params id=设备id；分页参数
     * @return 点检记录列表
     */
    public Page<CheckRecordDTO> checkRecord(Map<String, Object> params) {
        Page<CheckRecordDTO> page = new QueryPage<>(params);
        params.put("site", UserUtils.getSite());
        params.put("deviceId", new DeviceHandleBO(UserUtils.getSite(), (String) params.get("code")).getBo());
        List<CheckRecordDTO> list = checkExecuteMapper.checkRecord(page, params);
        if (CollUtil.isNotEmpty(list)) {
            List<String> groupIds = list.stream().map(CheckRecordDTO::getId).collect(Collectors.toList());
            //获取图片
            List<MesFiles> mesFiles = mesFilesService.listByGroupIds(groupIds);
            //获取数据收集组
            List<String> dataCollectionIds = list.stream().map(CheckRecordDTO::getDataCollectionId).distinct().collect(Collectors.toList());
            List<DataCollection> dataCollections = dataCollectionService.findByIds(dataCollectionIds);
            Map<String, String> dataCollectIdNameMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(dataCollections)) {
                dataCollectIdNameMap.putAll(dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, e -> StrUtil.isBlank(e.getName()) ? "" : e.getName())));
            }
            Map<String, List<MesFiles>> filesMap = new HashMap<>(16);
            if (CollUtil.isNotEmpty(mesFiles)) {
                filesMap.putAll(mesFiles.stream().collect(Collectors.groupingBy(MesFiles::getGroupId)));
            }
            list.forEach(e -> {
                if (filesMap.containsKey(e.getId())) {
                    List<MesFiles> itemList = filesMap.get(e.getId());
                    if (CollUtil.isNotEmpty(itemList)) {
                        e.setImgs(itemList.stream().map(MesFiles::getFilePath).collect(Collectors.toList()));
                    }
                }
                if (dataCollectIdNameMap.containsKey(e.getDataCollectionId())) {
                    e.setDataCollectionName(dataCollectIdNameMap.get(e.getDataCollectionId()));
                }
            });
        }
        page.setRecords(list);
        return page;
    }

    /**
     * 根据用户查询待点检工单数量
     *
     * @param userId 用户id
     * @param site   工厂id
     * @return 待点检工单数量
     */
    public Integer getCheckCountByUser(String userId, String site) {
        if (StrUtil.isBlank(userId) || StrUtil.isBlank(site)) {
            return 0;
        }
        return checkExecuteMapper.getCheckCountByUser(userId, site);
    }

    public void updateCheckExecuteStateByDeviceId(String planId, List<String> deviceIds, String user, Integer state) {
        CheckExecute checkExecute = new CheckExecute();
        checkExecute.setState(state);
        checkExecute.setUpdateBy(StrUtil.isBlank(user) ? "admin" : user);
        UpdateWrapper<CheckExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(CheckExecute::getDeviceId, deviceIds)
                .eq(CheckExecute::getCheckPlanId, planId)
                .eq(CheckExecute::getState, CheckStateEnum.DDZ.getCode());
        checkExecuteMapper.update(checkExecute, updateWrapper);
    }

    public void updateCheckExecuteState(ArrayList<String> code, Integer state) {
        CheckExecute checkExecute = new CheckExecute();
        checkExecute.setState(state);
        UpdateWrapper<CheckExecute> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().in(CheckExecute::getCode, code).eq(CheckExecute::getState, CheckStateEnum.DDZ.getCode());
        checkExecuteMapper.update(checkExecute, updateWrapper);
    }

    /**
     * 点检工单统计
     *
     * @param params 查询参数
     */
    public List<RepairExecuteCountStatisticsDTO> checkExecuteStatistics(Map<String, Object> params) {
        Assert.valid(params.get("startTime") == null, "开始时间不能为空");
        Assert.valid(params.get("endTime") == null, "结束时间不能为空");
        return checkExecuteMapper.checkExecuteStatistics(params);
    }
}
