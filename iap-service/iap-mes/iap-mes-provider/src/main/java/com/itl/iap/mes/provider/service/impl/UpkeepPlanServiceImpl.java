package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.LocalDateTimeUtils;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.UpkeepPlanDto;
import com.itl.iap.mes.api.dto.UpkeepPlanEnableDto;
import com.itl.iap.mes.api.dto.UpkeepPlanQueryDto;
import com.itl.iap.mes.api.dto.UpkeepPlanQueryDto1;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.vo.PlanTimeVo;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.DataCollectionMapper;
import com.itl.iap.mes.provider.mapper.JobMapper;
import com.itl.iap.mes.provider.mapper.UpkeepExecuteMapper;
import com.itl.iap.mes.provider.mapper.UpkeepPlanMapper;
import com.itl.iap.mes.provider.utils.CornUtils;
import com.itl.iap.mes.provider.utils.ScheduleUtils;
import com.itl.iap.mes.provider.utils.TimeUtils;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.DeviceService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class UpkeepPlanServiceImpl extends ServiceImpl<UpkeepPlanMapper, UpkeepPlan> {

    final private UpkeepPlanMapper upkeepPlanMapper;
    final private DeviceService deviceService;
    final private JobMapper jobMapper;
    final private UpkeepUserServiceImpl upkeepUserService;
    final private UpkeepDeviceServiceImpl upkeepDeviceService;
    final private DataCollectionMapper dataCollectionMapper;
    final private UserService userService;
    final private UpkeepExecuteMapper upkeepExecuteMapper;
    private final static String SORTASC = "ascending";


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static UpkeepPlan apply(UpkeepPlan x) {
        Integer ytd = x.getYtd();
        Integer cycle = x.getCycle();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(cycle);
        if (ytd.equals(0)) {
            stringBuffer.append("个月");
        } else if (ytd.equals(1)) {
            stringBuffer.append("天");
        } else {
            stringBuffer.append("小时");
        }
        x.setCycleName(stringBuffer.toString());
        return x;
    }

    @SneakyThrows
    @Transactional
    public void save(UpkeepPlanDto upkeepPlanDto, Integer type) throws CommonException {
        //编码唯一
        String planId = upkeepPlanDto.getId();
        String planNo = upkeepPlanDto.getPlanNo();
        QueryWrapper<UpkeepPlan> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(UpkeepPlan::getPlanNo, planNo);
        UpkeepPlan upkeepPlan = this.getOne(queryWrapper);
        if (upkeepPlan != null && !upkeepPlan.getId().equals(planId)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //开始时间必须早于结束时间
        if (upkeepPlanDto.getStartTime().getTime() >= upkeepPlanDto.getEndTime().getTime()) {
            throw new CommonException("开始时间必须早于结束时间", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (upkeepPlan == null) {
            upkeepPlan = new UpkeepPlan();
        }
        if (upkeepPlan.getId() == null) {
            upkeepPlanDto.setId(IdUtil.simpleUUID());
        } else {
            upkeepUserService.remove(Wrappers.<UpkeepUser>lambdaQuery().eq(UpkeepUser::getUpkeepId, planId));
            upkeepDeviceService.remove(Wrappers.<UpkeepDevice>lambdaQuery().eq(UpkeepDevice::getUpkeepId, planId));
        }
        //保存点检人,设备
        upkeepUserService.saveUpkeepUsers(upkeepPlanDto);
        upkeepDeviceService.saveCheckPlanDevices(upkeepPlanDto);
        BeanUtils.copyProperties(upkeepPlanDto, upkeepPlan);
        String site = UserUtils.getSite();
        upkeepPlan.setSite(site);
        upkeepPlan.setRunState(type);
        upkeepPlan.setState(1);
        if (1 == type) {
            Date startTimeDate = upkeepPlanDto.getStartTime();
            String startTime = sdf.format(startTimeDate);
            Integer cycle = upkeepPlanDto.getCycle();
            Integer ytd = upkeepPlanDto.getYtd();
            List<String> list = new ArrayList<>();
            if (ytd.equals(Constant.YtdEnum.MONTH.getItem())) {
                TimeUtils.digui(list, startTime, sdf.format(upkeepPlanDto.getEndTime()), cycle, Constant.YtdEnum.MONTH.getItem());
            } else if (ytd.equals(Constant.YtdEnum.DAY.getItem())) {
                TimeUtils.digui(list, startTime, sdf.format(upkeepPlanDto.getEndTime()), cycle, Constant.YtdEnum.DAY.getItem());
            } else {
                TimeUtils.digui(list, startTime, sdf.format(upkeepPlanDto.getEndTime()), cycle, Constant.YtdEnum.HOUR.getItem());
            }
            String ids = "";
            List<ScheduleJobEntity> jobEntities = new ArrayList<>();
            //增加最后一次定时任务用于过期任务不生成工单
            if (list.size() > 0) {
                String str = list.get(list.size() - 1);
                list.add(TimeUtils.nextMonth(str, cycle, ytd));
            }
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i);
                String corn = "";
                try {
                    corn = CornUtils.getCron(sdf.parse(str));
                } catch (ParseException e) {
                    throw new CustomException(CommonCode.PARSE_TIME_FAIL);
                }
                if (StringUtils.isNotBlank(corn)) {
                    ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
                    long jobId = IdUtil.getSnowflake(31, 31).nextId();
                    scheduleJobEntity.setJobId(jobId);
                    scheduleJobEntity.setCronExpression(corn);
                    scheduleJobEntity.setState(Constant.ScheduleStatus.NORMAL.getItem());
                    scheduleJobEntity.setBeanName("upkeepPlan");
                    scheduleJobEntity.setIsExecute("0");
                    scheduleJobEntity.setCreateTime(new Date());
                    Map<String, Object> params = new HashMap<>();
                    params.put("running", Constant.SYS_ADMIN);
                    params.put("upkeepPlan", upkeepPlan.getId());
                    params.put("operaStartTime", str);
                    params.put("jobId", jobId);
                    params.put("operaEndTime", TimeUtils.nextMonth(str, cycle, ytd));
                    if (i == list.size() - 1) {
                        params.put("isEnd", "1");//1 是 0否
                    } else {
                        params.put("isEnd", "0");//1 是 0否
                    }
                    scheduleJobEntity.setParams(JSONUtil.toJsonStr(params));
                    Scheduler scheduler = new StdSchedulerFactory().getScheduler();
                    CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, jobId);
                    //如果不存在，则创建
                    if (cronTrigger == null) {
                        ScheduleUtils.createScheduleJob(scheduler, scheduleJobEntity);
                    } else {
                        ScheduleUtils.updateScheduleJob(scheduler, scheduleJobEntity);
                    }
                    ids += "," + jobId;
                    jobEntities.add(scheduleJobEntity);
                }
            }
            jobMapper.insertBatch(jobEntities);
            upkeepPlan.setJobIds(ids);
        }
        this.saveOrUpdate(upkeepPlan);
    }


    public IPage<UpkeepPlan> findList(UpkeepPlanQueryDto upkeepPlanQueryDto, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        upkeepPlanQueryDto.setSite(UserUtils.getSite());
        //设备相关条件
        DeviceConditionDto deviceLikeDto = new DeviceConditionDto();
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceCode())) {
            deviceLikeDto.setDevice(upkeepPlanQueryDto.getDiviceCode());
        }
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceName())) {
            deviceLikeDto.setDeviceName(upkeepPlanQueryDto.getDiviceName());
        }
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceCodeOrName())) {
            deviceLikeDto.setDiviceCodeOrName(upkeepPlanQueryDto.getDiviceCodeOrName());
        }
        if (BeanUtil.isNotEmpty(deviceLikeDto)) {
            ResponseData<List<DevicePlusVo>> dataByLike = deviceService.getDataByCondition(deviceLikeDto);
            if (dataByLike != null && ResponseData.success().getCode().equals(dataByLike.getCode())) {
                List<DevicePlusVo> deviceList = dataByLike.getData();
                if (deviceList.size() == 0) {
                    return new Page<>();
                }
                List<String> bos = deviceList.stream().map(DevicePlusVo::getBo).collect(Collectors.toList());
                upkeepPlanQueryDto.setDiviceCondition(bos);
            }
        }
        QueryWrapper<CheckPlan> queryWrapper = QueryWrapperUtil.convertQuery(upkeepPlanQueryDto);
        String timeType = upkeepPlanQueryDto.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            String start = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            String end = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            queryWrapper.apply("not (startTime>{0} or endTime < {1})", end, start);
        }
        IPage<UpkeepPlan> ret = upkeepPlanMapper.findList(page, queryWrapper);
        List<UpkeepPlan> records = ret.getRecords();
        //待优化
        loadDevices(records);
        loadUpkeepUser(records);
        loadDataCollection(records);
        return ret;
    }

    public IPage<UpkeepPlan> findListByState(UpkeepPlan upkeepPlan, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        upkeepPlan.setSite(UserUtils.getSite());
        IPage<UpkeepPlan> ret = upkeepPlanMapper.findListByState(page, upkeepPlan);
        this.loadDevices(ret.getRecords());
        return ret;
    }

    public UpkeepPlan findById(String id) {
        UpkeepPlan upkeepPlan = upkeepPlanMapper.selectById(id);
        List<UpkeepPlan> es = ListUtil.toList(upkeepPlan);
        loadDataCollection(es);
        loadUpkeepUser(es);
        loadDevices(es);
        upkeepPlan = es.get(0);
        //获取执行的时间以及工单
        String jobIds = upkeepPlan.getJobIds();
        if (StrUtil.isNotBlank(jobIds)) {
            //获取任务列表
            QueryWrapper<ScheduleJobEntity> queryWrapper = new QueryWrapper<>();
            String[] jobIdList = jobIds.split(",");
            queryWrapper.lambda().in(ScheduleJobEntity::getJobId, jobIdList);
            List<ScheduleJobEntity> scheduleJobEntities = jobMapper.selectList(queryWrapper);
            //获取已经生成的工单
            QueryWrapper<UpkeepExecute> executeWrapper = new QueryWrapper<>();
            executeWrapper.lambda().eq(UpkeepExecute::getUpkeepPlanId, upkeepPlan.getId());
            List<UpkeepExecute> upkeepExecutes = upkeepExecuteMapper.selectList(executeWrapper);
            Map<String, List<UpkeepExecute>> collect = upkeepExecutes.stream().collect(Collectors.groupingBy(UpkeepExecute::getJobId));
            List<PlanTimeVo> result = new ArrayList<>();
            for (ScheduleJobEntity scheduleJobEntity : scheduleJobEntities) {
                PlanTimeVo planTimeVo = new PlanTimeVo();
                String params = scheduleJobEntity.getParams();
                JSONObject jsonObject = JSONUtil.parseObj(params);
                String operaStartTime = (String) jsonObject.get("operaStartTime");
                String operaEndTime = (String) jsonObject.get("operaEndTime");
                String isEnd = (String) jsonObject.get("isEnd");
                String jobId = String.valueOf(scheduleJobEntity.getJobId());
                planTimeVo.setStartTime(operaStartTime);
                planTimeVo.setEndTime(operaEndTime);
                if (collect.containsKey(jobId)) {
                    List<UpkeepExecute> upkeepExecuteList = collect.get(jobId);
                    String noList = upkeepExecuteList.stream().map(UpkeepExecute::getUpkeepNo).collect(Collectors.joining(","));
                    planTimeVo.setExecuteNo(noList);
                }
                if ("0".equals(isEnd)) {
                    result.add(planTimeVo);
                }
            }
            upkeepPlan.setPlanTimeData(result);
        }
        return upkeepPlan;
    }

    private void loadDataCollection(List<UpkeepPlan> records) {
        //数据收集组
        List<String> dataCollectionIds = records.stream().map(UpkeepPlan::getDataCollectionId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(dataCollectionIds)) {
            List<DataCollection> dataCollections = dataCollectionMapper.selectBatchIds(dataCollectionIds);
            Map<String, DataCollection> collect = dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, Function.identity()));
            for (UpkeepPlan record : records) {
                String dataCollectionId = record.getDataCollectionId();
                if (collect.containsKey(dataCollectionId)) {
                    record.setDataCollectionName(collect.get(dataCollectionId).getName());
                }
            }
        }
    }

    private void loadUpkeepUser(List<UpkeepPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(UpkeepPlan::getId).collect(Collectors.toList());
            List<UpkeepUser> upkeepUsers = upkeepUserService.list(new QueryWrapper<UpkeepUser>().lambda().in(UpkeepUser::getUpkeepId, idList));
            if (upkeepUsers != null && upkeepUsers.size() > 0) {
                Map<String, List<UpkeepUser>> map = upkeepUsers.stream().collect(Collectors.groupingBy(UpkeepUser::getUpkeepId));
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getId())) {
                        List<UpkeepUser> upkeepUserList = map.get(x.getId());
                        for (UpkeepUser upkeepUser : upkeepUserList) {
                            String upkeepUserId = upkeepUser.getUpkeepUserId();
                            ResponseData<IapSysUserT> user = userService.getUser(upkeepUserId);
                            if (user != null && ResultResponseEnum.SUCCESS.getCode().equals(user.getCode())
                                    && user.getData() != null) {
                                upkeepUser.setUpkeepUserName(user.getData().getRealName());
                            }
                        }
                        x.setUpkeepUsers(upkeepUserList);
                    }
                });
            }
        }
    }

    private void loadDevices(List<UpkeepPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(UpkeepPlan::getId).collect(Collectors.toList());
            List<UpkeepDevice> devices = upkeepDeviceService.list(new QueryWrapper<UpkeepDevice>().lambda().in(UpkeepDevice::getUpkeepId, idList));
            //查询设备
            List<String> deviceIdList = devices.stream().map(UpkeepDevice::getDeviceId).distinct().collect(Collectors.toList());
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(deviceIdList);
            ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
            Map<String, DevicePlusVo> collect = new HashMap<>();
            if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
                List<DevicePlusVo> data = dataByCondition.getData();
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
            if (devices != null && devices.size() > 0) {
                Map<String, List<UpkeepDevice>> map = devices.stream().collect(Collectors.groupingBy(UpkeepDevice::getUpkeepId));
                Map<String, DevicePlusVo> finalCollect = collect;
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getId())) {
                        List<UpkeepDevice> upkeepDevices = map.get(x.getId());
                        for (UpkeepDevice checkDevice : upkeepDevices) {
                            String deviceId = checkDevice.getDeviceId();
                            if (finalCollect.containsKey(deviceId)) {
                                DevicePlusVo device = finalCollect.get(deviceId);
                                checkDevice.setCode(device.getDevice());
                                checkDevice.setName(device.getDeviceName());
                                checkDevice.setType(device.getDeviceType());
                            }
                        }
                        x.setUpkeepDivices(upkeepDevices);
                    }
                });
            }
        }
    }


    @Transactional
    public void delete(List<String> ids) {
        List<String> idList = new ArrayList<>();
        ids.forEach(id -> {
            UpkeepPlan upkeepPlan = upkeepPlanMapper.selectById(id);
            Integer runState = upkeepPlan.getRunState();
            if (0 == runState) {
                idList.add(id);
            }
        });
        if (CollectionUtil.isNotEmpty(idList)) {
            upkeepUserService.remove(new QueryWrapper<UpkeepUser>().lambda().in(CollectionUtil.isNotEmpty(idList), UpkeepUser::getUpkeepId, idList));
            upkeepDeviceService.remove(new QueryWrapper<UpkeepDevice>().lambda().in(CollectionUtil.isNotEmpty(idList), UpkeepDevice::getUpkeepId, idList));
        }
        this.removeByIds(idList);
    }


    public void enable(UpkeepPlanEnableDto dto) {
        UpkeepPlan upkeepPlan = new UpkeepPlan();
        upkeepPlan.setState(dto.getState());
        upkeepPlanMapper.update(upkeepPlan, Wrappers.<UpkeepPlan>lambdaUpdate().in(UpkeepPlan::getId, dto.getIdList()));
    }

    public IPage<UpkeepPlan> findList1(UpkeepPlanQueryDto1 upkeepPlanQueryDto, Integer pageNum, Integer pageSize) {

        upkeepPlanQueryDto.setSite(UserUtils.getSite());
        //设备相关条件
        DeviceConditionDto deviceLikeDto = new DeviceConditionDto();
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceCode())) {
            deviceLikeDto.setDevice(upkeepPlanQueryDto.getDiviceCode());
        }
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceName())) {
            deviceLikeDto.setDeviceName(upkeepPlanQueryDto.getDiviceName());
        }
        if (StrUtil.isNotBlank(upkeepPlanQueryDto.getDiviceCodeOrName())) {
            deviceLikeDto.setDiviceCodeOrName(upkeepPlanQueryDto.getDiviceCodeOrName());
        }
        if (BeanUtil.isNotEmpty(deviceLikeDto)) {
            ResponseData<List<DevicePlusVo>> dataByLike = deviceService.getDataByCondition(deviceLikeDto);
            if (dataByLike != null && ResponseData.success().getCode().equals(dataByLike.getCode())) {
                List<DevicePlusVo> deviceList = dataByLike.getData();
                if (deviceList.size() == 0) {
                    return new Page<>();
                }
                List<String> bos = deviceList.stream().map(DevicePlusVo::getBo).collect(Collectors.toList());
                upkeepPlanQueryDto.setDiviceCondition(bos);
            }
        }
        QueryWrapper<CheckPlan> queryWrapper = QueryWrapperUtil.convertQuery(upkeepPlanQueryDto);
        String timeType = upkeepPlanQueryDto.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            String start = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            String end = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            queryWrapper.apply("not (startTime>{0} or endTime < {1})", end, start);
        }
        List<UpkeepPlan> records = upkeepPlanMapper.findAllList(queryWrapper);
        //待优化
        loadDevices1(records);
        loadUpkeepUser1(records);
        loadDataCollection(records);
        //将获取到的数据拼接周期
        records = records.stream().map(UpkeepPlanServiceImpl::apply).collect(Collectors.toList());
        //根据传参来排序
        sortNameDescOrAsc(upkeepPlanQueryDto, records);
        return this.getPages(pageNum, pageSize, records);


    }

    /**
     * 根据字段排序
     *
     * @param upkeepPlanQueryDto upkeepPlanQueryDto
     * @param records records
     * @return List.clas
     */
    private  List<UpkeepPlan> sortNameDescOrAsc(UpkeepPlanQueryDto1 upkeepPlanQueryDto, List<UpkeepPlan> records) {
        String sortName = upkeepPlanQueryDto.getSortName();
        String sortType = upkeepPlanQueryDto.getSortType();
        switch (sortName) {
            case "planNo":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getPlanNo, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getPlanNo, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());

                }
                break;

            case "upkeepPlanName":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getUpkeepPlanName, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getUpkeepPlanName, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "dataCollectionName":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getDataCollectionName, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getDataCollectionName, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());

                }
                break;
            case "cycle1":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getCycleName, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getCycleName, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "type":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getCode, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getCode, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "code1":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getName, Comparator.nullsLast(String::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getName, Comparator.nullsLast(String::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "startTime":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getStartTime, Comparator.nullsLast(Date::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getStartTime, Comparator.nullsLast(Date::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "endTime":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getEndTime, Comparator.nullsLast(Date::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getEndTime, Comparator.nullsLast(Date::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "state":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getState, Comparator.nullsLast(Integer::compareTo))).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getState, Comparator.nullsLast(Integer::compareTo)).reversed()).collect(Collectors.toList());
                }
                break;
            case "upkeepUserName":
                if (SORTASC.equalsIgnoreCase(sortType)) {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getUpkeepUserName)).collect(Collectors.toList());

                } else {
                    records = records.stream().sorted(Comparator.comparing(UpkeepPlan::getUpkeepUserName).reversed()).collect(Collectors.toList());
                }
                break;
            default:
                break;
        }
        return records;
    }


    private IPage getPages(Integer currentPage, Integer pageSize, List<UpkeepPlan> list) {
        IPage page = new Page();
        if (CollectionUtil.isEmpty(list)) {
            return null;
        }
        int size = list.size();

        if (pageSize > size) {
            pageSize = size;
        }
        if (pageSize != 0) {
            // 求出最大页数，防止currentPage越界
            int maxPage = size % pageSize == 0 ? size / pageSize : size / pageSize + 1;

            if (currentPage > maxPage) {
                currentPage = maxPage;
            }
        }
        // 当前页第一条数据的下标
        int curIdx = currentPage > 1 ? (currentPage - 1) * pageSize : 0;

        List pageList = new ArrayList();

        // 将当前页的数据放进pageList
        for (int i = 0; i < pageSize && curIdx + i < size; i++) {
            pageList.add(list.get(curIdx + i));
        }

        page.setCurrent(currentPage).setSize(pageSize).setTotal(list.size()).setRecords(pageList);
        return page;
    }

    private void loadUpkeepUser1(List<UpkeepPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(UpkeepPlan::getId).collect(Collectors.toList());
            List<UpkeepUser> upkeepUsers = upkeepUserService.list(new QueryWrapper<UpkeepUser>().lambda().in(UpkeepUser::getUpkeepId, idList));
            if (upkeepUsers != null && upkeepUsers.size() > 0) {
                Map<String, List<UpkeepUser>> map = upkeepUsers.stream().collect(Collectors.groupingBy(UpkeepUser::getUpkeepId));
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getId())) {
                        List<String> upkeepUserNameList = new ArrayList<>();
                        List<UpkeepUser> upkeepUserList = map.get(x.getId());
                        for (UpkeepUser upkeepUser : upkeepUserList) {
                            String upkeepUserId = upkeepUser.getUpkeepUserId();
                            ResponseData<IapSysUserT> user = userService.getUser(upkeepUserId);
                            if (user != null && ResultResponseEnum.SUCCESS.getCode().equals(user.getCode())) {
                                upkeepUser.setUpkeepUserName(user.getData().getRealName());
                                upkeepUserNameList.add(user.getData().getRealName());
                            }
                        }
                        x.setUpkeepUsers(upkeepUserList);
                        if (CollectionUtil.isNotEmpty(upkeepUserNameList)) {

                            String upkeepUserNames = StringUtils.join(upkeepUserNameList.toArray(), ", ");
                            x.setUpkeepUserName(upkeepUserNames);
                        }
                    }
                });
            }
        }
    }

    private void loadDevices1(List<UpkeepPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(UpkeepPlan::getId).collect(Collectors.toList());
            List<UpkeepDevice> devices = upkeepDeviceService.list(new QueryWrapper<UpkeepDevice>().lambda().in(UpkeepDevice::getUpkeepId, idList));
            //查询设备
            List<String> deviceIdList = devices.stream().map(UpkeepDevice::getDeviceId).distinct().collect(Collectors.toList());
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(deviceIdList);
            ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
            Map<String, DevicePlusVo> collect = new HashMap<>();
            if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
                List<DevicePlusVo> data = dataByCondition.getData();
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
            if (devices != null && devices.size() > 0) {
                Map<String, List<UpkeepDevice>> map = devices.stream().collect(Collectors.groupingBy(UpkeepDevice::getUpkeepId));
                Map<String, DevicePlusVo> finalCollect = collect;

                records.parallelStream().forEach(x -> {

                    if (map.containsKey(x.getId())) {
                        List<String> nameList = new ArrayList<>();
                        List<String> codeList = new ArrayList<>();
                        List<UpkeepDevice> upkeepDevices = map.get(x.getId());
                        for (UpkeepDevice checkDevice : upkeepDevices) {
                            String deviceId = checkDevice.getDeviceId();
                            if (finalCollect.containsKey(deviceId)) {
                                DevicePlusVo device = finalCollect.get(deviceId);
                                String deviceName = device.getDeviceName();
                                String device1 = device.getDevice();
                                checkDevice.setCode(device1);
                                checkDevice.setName(deviceName);
                                checkDevice.setType(device.getDeviceType());
                                if (StringUtils.isNotBlank(deviceName)) {
                                    nameList.add(deviceName);
                                }
                                if (StringUtils.isNotBlank(device1)) {
                                    codeList.add(device1);
                                }
                            }
                        }
                        x.setUpkeepDivices(upkeepDevices);
                        if (CollectionUtil.isNotEmpty(codeList)) {

                            String codes = StringUtils.join(codeList.toArray(), ", ");
                            x.setCode(codes);
                        }
                        if (CollectionUtil.isNotEmpty(nameList)) {
                            String names = StringUtils.join(nameList.toArray(), ", ");
                            x.setName(names);
                        }
                    }
                });

            }
        }
    }
}
