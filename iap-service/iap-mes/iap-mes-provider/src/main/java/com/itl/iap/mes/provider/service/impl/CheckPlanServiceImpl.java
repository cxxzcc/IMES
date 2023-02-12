package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.PageUtil;
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
import com.itl.iap.common.util.PageUtils;
import com.itl.iap.mes.api.dto.CheckPlanDto;
import com.itl.iap.mes.api.dto.CheckPlanEnableDto;
import com.itl.iap.mes.api.dto.CheckPlanQueryDto;
import com.itl.iap.mes.api.entity.*;
import com.itl.iap.mes.api.vo.PlanTimeVo;
import com.itl.iap.mes.provider.common.CommonCode;
import com.itl.iap.mes.provider.config.Constant;
import com.itl.iap.mes.provider.exception.CustomException;
import com.itl.iap.mes.provider.mapper.CheckExecuteMapper;
import com.itl.iap.mes.provider.mapper.CheckPlanMapper;
import com.itl.iap.mes.provider.mapper.DataCollectionMapper;
import com.itl.iap.mes.provider.mapper.JobMapper;
import com.itl.iap.mes.provider.utils.CornUtils;
import com.itl.iap.mes.provider.utils.ScheduleUtils;
import com.itl.iap.mes.provider.utils.SnowFlake;
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
import org.springframework.cloud.commons.util.IdUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CheckPlanServiceImpl extends ServiceImpl<CheckPlanMapper, CheckPlan> {

    final private CheckPlanMapper checkPlanMapper;
    final private JobMapper jobMapper;
    final private CheckPlanUserServiceImpl checkPlanUserService;
    final private CheckDeviceServiceImpl checkDeviceService;
    final private CheckExecuteMapper checkExecuteMapper;
    final private DeviceService deviceService;
    final private UserService userService;
    final private DataCollectionMapper dataCollectionMapper;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Transactional
    @SneakyThrows
    public void save(CheckPlanDto checkPlanDto, Integer type) {
        //编码唯一
        String planId = checkPlanDto.getId();
        String planNo = checkPlanDto.getPlanNo();
        QueryWrapper<CheckPlan> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().eq(CheckPlan::getPlanNo, planNo);
        CheckPlan checkPlan = this.getOne(queryWrapper);
        if (checkPlan != null && !checkPlan.getId().equals(planId)) {
            throw new CommonException("编码不能重复", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        //开始时间必须早于结束时间
        if (checkPlanDto.getStartTime().getTime() >= checkPlanDto.getEndTime().getTime()) {
            throw new CommonException("开始时间必须早于结束时间", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        if (checkPlan == null) {
            checkPlan = new CheckPlan();
        }
        if (checkPlan.getId() == null) {
            checkPlanDto.setId(IdUtil.simpleUUID());
        } else {
            checkPlanUserService.remove(Wrappers.<CheckPlanUser>lambdaQuery().eq(CheckPlanUser::getCheckId, planId));
            checkDeviceService.remove(Wrappers.<CheckDevice>lambdaQuery().eq(CheckDevice::getCheckId, planId));
        }
        //保存点检人,设备
        checkPlanUserService.saveCheckPlanUsers(checkPlanDto);
        checkDeviceService.saveCheckPlanDevices(checkPlanDto);
        BeanUtil.copyProperties(checkPlanDto, checkPlan);
        String site = UserUtils.getSite();
        checkPlan.setSiteId(site);
        checkPlan.setRunState(type);
        checkPlan.setState(1);
        if (1 == type) {
            Date startTimeDate = checkPlanDto.getStartTime();
            String startTime = sdf.format(startTimeDate);
            Integer cycle = checkPlanDto.getCycle();
            Integer ytd = checkPlanDto.getYtd();
            List<String> list = new ArrayList<>();
            if (ytd.equals(Constant.YtdEnum.MONTH.getItem())) {
                TimeUtils.digui(list, startTime, sdf.format(checkPlanDto.getEndTime()), cycle, Constant.YtdEnum.MONTH.getItem());
            } else if (ytd.equals(Constant.YtdEnum.DAY.getItem())) {
                TimeUtils.digui(list, startTime, sdf.format(checkPlanDto.getEndTime()), cycle, Constant.YtdEnum.DAY.getItem());
            } else {
                TimeUtils.digui(list, startTime, sdf.format(checkPlanDto.getEndTime()), cycle, Constant.YtdEnum.HOUR.getItem());
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
                    scheduleJobEntity.setBeanName("checkPlan");
                    scheduleJobEntity.setCreateTime(new Date());
                    scheduleJobEntity.setIsExecute("0");
                    Map<String, Object> params = new HashMap<>();
                    params.put("running", Constant.SYS_ADMIN);
                    params.put("jobId", jobId);
                    params.put("checkPlan", checkPlan.getId());
                    params.put("operaStartTime", str);
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
            checkPlan.setJobIds(ids);
        }
        this.saveOrUpdate(checkPlan);
    }


    public IPage<CheckPlan> findList(CheckPlanQueryDto checkPlanQueryDto, Integer pageNum, Integer pageSize) {
        checkPlanQueryDto.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum, pageSize);
        //设备相关条件
        DeviceConditionDto deviceLikeDto = new DeviceConditionDto();
        if (StrUtil.isNotBlank(checkPlanQueryDto.getDiviceCode())) {
            deviceLikeDto.setDevice(checkPlanQueryDto.getDiviceCode());
        }
        if (StrUtil.isNotBlank(checkPlanQueryDto.getDiviceName())) {
            deviceLikeDto.setDeviceName(checkPlanQueryDto.getDiviceName());
        }
        if (StrUtil.isNotBlank(checkPlanQueryDto.getDiviceCodeOrName())) {
            deviceLikeDto.setDiviceCodeOrName(checkPlanQueryDto.getDiviceCodeOrName());
        }
        if (BeanUtil.isNotEmpty(deviceLikeDto)) {
            ResponseData<List<DevicePlusVo>> dataByLike = deviceService.getDataByCondition(deviceLikeDto);
            if (dataByLike != null && ResponseData.success().getCode().equals(dataByLike.getCode())) {
                List<DevicePlusVo> deviceList = dataByLike.getData();
                if (deviceList.size() == 0) {
                    return new Page<>();
                }
                List<String> bos = deviceList.stream().map(DevicePlusVo::getBo).collect(Collectors.toList());
                checkPlanQueryDto.setDiviceCondition(bos);
            }
        }
        QueryWrapper<CheckPlan> queryWrapper = QueryWrapperUtil.convertQuery(checkPlanQueryDto);
        String timeType = checkPlanQueryDto.getTimeType();
        LocalDateTimeUtils.DateTimeScope dateTimeScope = LocalDateTimeUtils.getDateTimeScope(timeType);
        if (dateTimeScope != null) {
            String start = LocalDateTimeUtil.format(dateTimeScope.getStartTime(), DatePattern.NORM_DATETIME_PATTERN);
            String end = LocalDateTimeUtil.format(dateTimeScope.getEndTime(), DatePattern.NORM_DATETIME_PATTERN);
            queryWrapper.apply("not (startTime>{0} or endTime < {1})", end, start);
        }
        IPage<CheckPlan> ret = checkPlanMapper.findList(page, queryWrapper);
        List<CheckPlan> records = ret.getRecords();
        //待优化
        loadDevices(records);
        loadCheckUser(records);
        loadDataCollection(records);
        return ret;
    }


    public IPage<CheckPlan> findListByState(CheckPlan checkPlan, Integer pageNum, Integer pageSize) {
        checkPlan.setSiteId(UserUtils.getSite());
        Page page = new Page(pageNum, pageSize);
        IPage<CheckPlan> ret = checkPlanMapper.findListByState(page, checkPlan);
        loadDevices(ret.getRecords());
        loadCheckUser(ret.getRecords());
        return ret;
    }

    private void loadDataCollection(List<CheckPlan> records) {
        //数据收集组
        List<String> dataCollectionIds = records.stream().map(CheckPlan::getDataCollectionId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(dataCollectionIds)) {
            List<DataCollection> dataCollections = dataCollectionMapper.selectBatchIds(dataCollectionIds);
            Map<String, DataCollection> collect = dataCollections.stream().collect(Collectors.toMap(DataCollection::getId, Function.identity()));
            for (CheckPlan record : records) {
                String dataCollectionId = record.getDataCollectionId();
                if (collect.containsKey(dataCollectionId)) {
                    record.setDataCollectionName(collect.get(dataCollectionId).getName());
                }
            }
        }
    }

    private void loadDevices(List<CheckPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(CheckPlan::getId).collect(Collectors.toList());
            List<CheckDevice> devices = checkDeviceService.list(new QueryWrapper<CheckDevice>().lambda().in(CheckDevice::getCheckId, idList));
            //查询设备
            List<String> deviceIdList = devices.stream().map(CheckDevice::getDeviceId).distinct().collect(Collectors.toList());
            DeviceConditionDto conditionDto = new DeviceConditionDto();
            conditionDto.setBoList(deviceIdList);
            ResponseData<List<DevicePlusVo>> dataByCondition = deviceService.getDataByCondition(conditionDto);
            Map<String, DevicePlusVo> collect = new HashMap<>();
            if (dataByCondition != null && ResponseData.success().getCode().equals(dataByCondition.getCode())) {
                List<DevicePlusVo> data = dataByCondition.getData();
                collect = data.stream().collect(Collectors.toMap(DevicePlusVo::getBo, Function.identity()));
            }
            if (devices != null && devices.size() > 0) {
                Map<String, List<CheckDevice>> map = devices.stream().collect(Collectors.groupingBy(CheckDevice::getCheckId));
                Map<String, DevicePlusVo> finalCollect = collect;
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getId())) {
                        List<CheckDevice> checkDevices = map.get(x.getId());
                        for (CheckDevice checkDevice : checkDevices) {
                            String deviceId = checkDevice.getDeviceId();
                            if (finalCollect.containsKey(deviceId)) {
                                DevicePlusVo device = finalCollect.get(deviceId);
                                checkDevice.setCode(device.getDevice());
                                checkDevice.setName(device.getDeviceName());
                                checkDevice.setType(device.getDeviceType());
                            }
                        }
                        x.setCheckDevice(checkDevices);
                    }
                });
            }
        }
    }


    private void loadCheckUser(List<CheckPlan> records) {
        if (records != null && records.size() > 0) {
            List<String> idList = records.stream().map(CheckPlan::getId).collect(Collectors.toList());
            List<CheckPlanUser> checkPlanUsers = checkPlanUserService.list(new QueryWrapper<CheckPlanUser>().lambda().in(CheckPlanUser::getCheckId, idList));
            if (checkPlanUsers != null && checkPlanUsers.size() > 0) {
                Map<String, List<CheckPlanUser>> map = checkPlanUsers.stream().collect(Collectors.groupingBy(CheckPlanUser::getCheckId));
                records.parallelStream().forEach(x -> {
                    if (map.containsKey(x.getId())) {
                        List<CheckPlanUser> checkPlanUserList = map.get(x.getId());
                        for (CheckPlanUser checkPlanUser : checkPlanUserList) {
                            String checkUserId = checkPlanUser.getCheckUserId();
                            ResponseData<IapSysUserT> user = userService.getUser(checkUserId);
                            if (user != null && ResultResponseEnum.SUCCESS.getCode().equals(user.getCode())
                                    && user.getData() != null) {
                                checkPlanUser.setCheckUserName(user.getData().getRealName());
                            }
                        }
                        x.setCheckUsers(checkPlanUserList);
                    }
                });
            }
        }
    }

    @Transactional
    public void delete(List<String> ids) {
        List<String> idList = new ArrayList<>();
        ids.forEach(id -> {
            CheckPlan checkPlan = checkPlanMapper.selectById(id);
            Integer runState = checkPlan.getRunState();
            if (0 == runState) {
                idList.add(id);
            }
//            String jobIds = checkPlan.getJobIds();
//            if (StringUtils.isNotBlank(jobIds)) {
//                List<String> stringList = Arrays.asList(jobIds.split(","));
//                Scheduler scheduler = null;
//                try {
//                    scheduler = new StdSchedulerFactory().getScheduler();
//                } catch (SchedulerException e) {
//                    throw new CustomException(CommonCode.SUCCESS);
//                }
//                for (String jobId : stringList) {
//                    if (StringUtils.isNotBlank(jobId)) {
//                        ScheduleUtils.deleteScheduleJob(scheduler, Long.parseLong(jobId));
//                        jobMapper.deleteById(Long.parseLong(jobId));
//                    }
//                }
//            }
        });
        if (CollectionUtil.isNotEmpty(idList)) {
            checkPlanUserService.remove(new QueryWrapper<CheckPlanUser>().lambda().in(CollectionUtil.isNotEmpty(idList), CheckPlanUser::getCheckId, idList));
            checkDeviceService.remove(new QueryWrapper<CheckDevice>().lambda().in(CollectionUtil.isNotEmpty(idList), CheckDevice::getCheckId, idList));
            this.removeByIds(idList);
        }
    }


    public CheckPlan findById(String id) {
        CheckPlan checkPlan = checkPlanMapper.selectById(id);
        List<CheckPlan> es = ListUtil.toList(checkPlan);
        loadDataCollection(es);
        loadCheckUser(es);
        loadDevices(es);
        checkPlan = es.get(0);
        //获取执行的时间以及工单
        String jobIds = checkPlan.getJobIds();
        if (StrUtil.isNotBlank(jobIds)) {
            //获取任务列表
            QueryWrapper<ScheduleJobEntity> queryWrapper = new QueryWrapper<>();
            String[] jobIdList = jobIds.split(",");
            queryWrapper.lambda().in(ScheduleJobEntity::getJobId, jobIdList);
            List<ScheduleJobEntity> scheduleJobEntities = jobMapper.selectList(queryWrapper);
            //获取已经生成的工单
            QueryWrapper<CheckExecute> executeWrapper = new QueryWrapper<>();
            executeWrapper.lambda().eq(CheckExecute::getCheckPlanId, checkPlan.getId());
            List<CheckExecute> checkExecutes = checkExecuteMapper.selectList(executeWrapper);
            Map<String, List<CheckExecute>> collect = checkExecutes.stream().collect(Collectors.groupingBy(CheckExecute::getJobId));
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
                    List<CheckExecute> checkExecuteList = collect.get(jobId);
                    String noList = checkExecuteList.stream().map(CheckExecute::getCheckNo).collect(Collectors.joining(","));
                    planTimeVo.setExecuteNo(noList);
                }
                if ("0".equals(isEnd)) {
                    result.add(planTimeVo);
                }
            }
            checkPlan.setPlanTimeData(result);
        }

        return checkPlan;
    }

    public void enable(CheckPlanEnableDto dto) {
        CheckPlan checkPlan = new CheckPlan();
        checkPlan.setState(dto.getState());
        checkPlanMapper.update(checkPlan, Wrappers.<CheckPlan>lambdaUpdate().in(CheckPlan::getId, dto.getIdList()));
    }
}
