package com.itl.mes.andon.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.itl.iap.attachment.client.service.UserService;
import com.itl.iap.common.base.dto.UserTDto;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.common.util.StringUtils;
import com.itl.iap.notice.client.NoticeService;
import com.itl.iap.system.api.entity.IapSysUserT;
import com.itl.mes.andon.api.dto.AndonSaveRepairCallBackDTO;
import com.itl.mes.andon.api.dto.RecordSaveDTO;
import com.itl.mes.andon.api.entity.Andon;
import com.itl.mes.andon.api.entity.Box;
import com.itl.mes.andon.api.entity.Record;
import com.itl.mes.andon.api.service.AndonService;
import com.itl.mes.andon.api.service.AndonTriggerService;
import com.itl.mes.andon.api.vo.AndonTriggerAndonVo;
import com.itl.mes.andon.api.vo.AndonTriggerPushUserVo;
import com.itl.mes.andon.api.vo.AndonTriggerVo;
import com.itl.mes.andon.api.vo.RecordVo;
import com.itl.mes.andon.provider.common.CommonCode;
import com.itl.mes.andon.provider.common.CommonUtils;
import com.itl.mes.andon.provider.config.Constant;
import com.itl.mes.andon.provider.exception.CustomException;
import com.itl.mes.andon.provider.mapper.AndonTriggerMapper;
import com.itl.mes.andon.provider.mapper.BoxMapper;
import com.itl.mes.andon.provider.mapper.RecordMapper;
import com.itl.mes.core.client.service.CodeRuleService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Service
public class AndonTriggerServiceImpl implements AndonTriggerService {

    @Autowired
    AndonTriggerMapper andonTriggerMapper;

    @Autowired
    BoxMapper boxMapper;

    @Autowired
    AndonService andonService;

    @Autowired
    NoticeService noticeService;

    @Autowired
    RecordMapper recordMapper;

    @Value("${image.uploadpath}")
    private String uploadPath;

    @Value("${notice.itemTemplateCode}")
    private String itemTemplateCode;

    @Value("${notice.deviceTemplateCode}")
    private String deviceTemplateCode;

    @Autowired
    CodeRuleService codeRuleService;

    @Override
    public List<Map<String, Object>> findAndonList(String stationBo) {
        String site = UserUtils.getSite();
        // Constant
        AndonTriggerVo andonTriggerVo = new AndonTriggerVo();
        andonTriggerVo.setUserName(Objects.requireNonNull(UserUtils.getCurrentUser()).getUserName());
//        String userId = UserUtils.getCurrentUser().getId();

        //由于一个用户可能绑定多个工位，目前功能只实现只有一个工位的情况，默认取查询到的第一个工位数据
//        List<Map<String, String>> stationList = andonTriggerMapper.getStationList(userId, UserUtils.getSite());
//        if (CollUtil.isEmpty(stationList)) {
//            throw new CustomException(CommonCode.STATION_NOT_FOUND);
//        }
//        String stationBo = stationList.get(0).get("stationBo");

        // 查找 工位 产线 车间 设备 绑定的安灯信息。在此基础上还分"使用灯箱"和"不使用灯箱"两种情况,(工位绑定时：要么只绑定灯箱，要么只绑定具体的安灯)。
        // 工位
        QueryWrapper<Box> boxQueryWrapper = new QueryWrapper<>();
        boxQueryWrapper.eq("station_bo", stationBo);
        List<Box> boxList = boxMapper.selectList(boxQueryWrapper);
        Set<AndonTriggerAndonVo> andonTriggerAndonVos = new HashSet<>();
        if (CollUtil.isNotEmpty(boxList)) {
            andonTriggerVo.setBoxName("工位灯箱");
            // 有灯箱,获取灯箱Bo
            Set<String> collect = boxList.stream().map(Box::getBo).collect(Collectors.toSet());
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, collect, "", "", "", ""));
        } else {
            // 无灯箱
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, null, stationBo, "", "", ""));
        }

        // 产线
        String productLineBo = andonTriggerMapper.getProductLineBo(stationBo);
        QueryWrapper<Box> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("product_line_bo", productLineBo);
        List<Box> listByProductLineBo = boxMapper.selectList(queryWrapper);
        if (CollUtil.isNotEmpty(listByProductLineBo)) {
            // 有灯箱
            if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
                andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",产线灯箱");
            } else {
                andonTriggerVo.setBoxName("产线灯箱");
            }
            Set<String> collect = listByProductLineBo.stream().map(Box::getBo).collect(Collectors.toSet());
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, collect, "", "", "", ""));
        } else {
            // 没灯箱
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, null, "", productLineBo, "", ""));
        }

        // 设备灯箱 todo (根据工位获取设备,或者通过其他方式获取,目前表关系混乱通过这种方式获取)
        QueryWrapper<Box> boxStationWrapper = new QueryWrapper<>();
        boxStationWrapper.eq("RESOURCE_TYPE", "设备");
        List<Box> boxListByStation = boxMapper.selectList(boxStationWrapper);
        if (CollUtil.isNotEmpty(boxListByStation)) {
            Set<String> collect = boxListByStation.stream().filter(box -> StringUtils.isNotEmpty(box.getDeviceBo())).map(Box::getBo).collect(Collectors.toSet());
            if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
                andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",设备灯箱");
            } else {
                andonTriggerVo.setBoxName("设备灯箱");
            }
            if (CollUtil.isNotEmpty(collect)) {
                // TODO: 2021/12/15  获取设备Bo
                andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, collect, "", "", "", ""));
            }
        }

        // 车间
        String workShopBo = andonTriggerMapper.getWorkShopBo(productLineBo);
        QueryWrapper<Box> wrapper = new QueryWrapper<>();
        wrapper.eq("work_shop_bo", workShopBo);
        List<Box> listByWorkShopBo = boxMapper.selectList(wrapper);
        if (CollUtil.isNotEmpty(listByWorkShopBo)) {
            if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
                andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",车间灯箱");
            } else {
                andonTriggerVo.setBoxName("车间灯箱");
            }
            Set<String> collect = listByWorkShopBo.stream().map(Box::getBo).collect(Collectors.toSet());
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, collect, "", "", "", ""));
        } else {
            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(site, null, "", "", workShopBo, ""));
        }

        List<AndonTriggerAndonVo> deleteTriggerAndonVos = new ArrayList<>();
        for (AndonTriggerAndonVo andonTriggerAndonVo : andonTriggerAndonVos) {
            if (andonTriggerAndonVo.getState().equals(Constant.recordState.TRIGGER.getValue())) {
                andonTriggerAndonVos.forEach(triggerAndonVo -> {
                    if (triggerAndonVo.getState().equals(Constant.recordState.REPAIT.getValue()) && andonTriggerAndonVo.getAndonBo().equals(triggerAndonVo.getAndonBo())) {
                        deleteTriggerAndonVos.add(triggerAndonVo);
                    }
                });
            }
        }
        deleteTriggerAndonVos.forEach(andonTriggerAndonVos::remove);
        andonTriggerVo.setAndonTriggerAndonVos(andonTriggerAndonVos);
        Map<String, List<AndonTriggerAndonVo>> map = andonTriggerAndonVos.stream().collect(Collectors.groupingBy(AndonTriggerAndonVo::getAndonTypeBo));
        List<Map<String, Object>> ret = new ArrayList<>(map.size());
        map.forEach((k, v) -> {
            ret.add(new HashMap<String, Object>(4) {{
                put("typeBo", k);
                AndonTriggerAndonVo vo = v.get(0);
                put("type", vo.getAndonType());
                put("typeName", vo.getAndonTypeName());
                put("list", v.stream().collect(
                        Collectors.collectingAndThen(
                                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(AndonTriggerAndonVo::getAndonBo))), ArrayList::new)
                ));
            }});
        });
        return ret;
    }

    // 默认查询所有工位的
//    @Override
//    public AndonTriggerVo findAndonList() {
//
//        AndonTriggerVo andonTriggerVo = new AndonTriggerVo();
//        andonTriggerVo.setUserName(UserUtils.getCurrentUser().getUserName());
//        String userId = UserUtils.getCurrentUser().getId();
//
//        //由于一个用户可能绑定多个工位，目前功能只实现只有一个工位的情况，默认取查询到的第一个工位数据 todo 这样逻辑是有错误的 已改正
//        List<Map<String, String>> stationList = andonTriggerMapper.getStationList(userId, UserUtils.getSite());
//        if (CollUtil.isEmpty(stationList)) {
//            throw new CustomException(CommonCode.STATION_NOT_FOUND);
//        }
//        Set<String> setStationBo = stationList.stream().map(m -> m.get("stationBo")).collect(Collectors.toSet());
//        //        String stationBo = stationList.get(0).get("stationBo");
//        if (CollUtil.isEmpty(setStationBo)) {
//            throw new CustomException(CommonCode.STATION_NOT_FOUND);
//        }
//
//        QueryWrapper<Box> boxQueryWrapper = new QueryWrapper<>();
//        boxQueryWrapper.in("station_bo", setStationBo);
//        boxQueryWrapper.select("bo");
//        Integer count = 0;
//        count = boxMapper.selectCount(boxQueryWrapper);
//
//        List<String> boxBoList = new ArrayList<>();
//        //校验工位的灯箱是否有数据，没有则查找产线，再没有则查找车间
//        Set<AndonTriggerAndonVo> andonTriggerAndonVos = new HashSet<>();
//        if (count > 0) {
//            andonTriggerVo.setBoxName("工位灯箱");
//            List<Box> boxList = boxMapper.selectList(boxQueryWrapper);
//            boxList.forEach(box -> {
//                boxBoList.add(box.getBo());
//            });
//            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));
//
//        }
////        select PRODUCT_LINE_BO as productLineBo from m_station where bo = #{stationBo}
//        List<String> productLineBos = andonTriggerMapper.getLineBo(setStationBo);
//
//        QueryWrapper<Box> queryWrapper = new QueryWrapper<>();
//        queryWrapper.select("bo");
//        queryWrapper.in("product_line_bo", productLineBos);
//        count = boxMapper.selectCount(queryWrapper);
//        if (count > 0) {
//            if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
//                andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",产线灯箱");
//            } else {
//                andonTriggerVo.setBoxName("产线灯箱");
//            }
//            List<Box> boxList = boxMapper.selectList(queryWrapper);
//            boxList.forEach(box -> {
//                boxBoList.add(box.getBo());
//            });
//            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));
//        }
//
////        String workShopBo = andonTriggerMapper.getWorkShopBo(productLineBo);
//        List<String> workShopBo = andonTriggerMapper.getWorkShopBoList(productLineBos);
//
//        QueryWrapper<Box> wrapper = new QueryWrapper<>();
//        wrapper.select("bo");
//        wrapper.in("work_shop_bo", workShopBo);
//        List<Box> boxList = boxMapper.selectList(wrapper);
//        boxList.forEach(box -> {
//            boxBoList.add(box.getBo());
//        });
//        if (CollUtil.isNotEmpty(boxBoList)) {
//            if (StrUtil.isNotBlank(andonTriggerVo.getBoxName())) {
//                andonTriggerVo.setBoxName(andonTriggerVo.getBoxName() + ",车间灯箱");
//            } else {
//                andonTriggerVo.setBoxName("车间灯箱");
//            }
//            andonTriggerAndonVos.addAll(andonTriggerMapper.findList(boxBoList));
//        }
//
//
//        List<AndonTriggerAndonVo> deleteTriggerAndonVos = new ArrayList<>();
//        for (AndonTriggerAndonVo andonTriggerAndonVo : andonTriggerAndonVos) {
//            if (andonTriggerAndonVo.getState().equals(Constant.recordState.TRIGGER.getValue())) {
//                andonTriggerAndonVos.forEach(triggerAndonVo -> {
//                    if (triggerAndonVo.getState().equals(Constant.recordState.REPAIT.getValue())
//                            && andonTriggerAndonVo.getAndonBo().equals(triggerAndonVo.getAndonBo())) {
//                        deleteTriggerAndonVos.add(triggerAndonVo);
//                    }
//                });
//            } else {
//                continue;
//            }
//        }
//
//        deleteTriggerAndonVos.forEach(andonTriggerAndonVos::remove);
//        andonTriggerVo.setAndonTriggerAndonVos(andonTriggerAndonVos);
//        return andonTriggerVo;
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveRecord(RecordSaveDTO recordSaveDTO) {
        Andon andon = Optional.ofNullable(
                andonService.getById(
                        Objects.requireNonNull(recordSaveDTO.getAndonBo(), "未接收到安灯设置信息!")
                )
        ).orElseThrow(() -> new RuntimeException("获取安灯设置信息异常!"));
        Record record = new Record();
        String site = UserUtils.getSite();
        // 呼叫人 取username(用户账号)多个以,号拼接
        record.setCallMan(recordSaveDTO.getCallMan());
        record.setSite(site);
        record.setAndonBo(recordSaveDTO.getAndonBo());
        record.setState(Constant.recordState.TRIGGER.getValue());
        record.setDeviceBo(recordSaveDTO.getDeviceBo());
        record.setUrgencyLevel(recordSaveDTO.getUrgencyLevel());
        record.setState("1");

        if (StrUtil.isNotBlank(recordSaveDTO.getAbnormalRemark())) {
            record.setAbnormalRemark(recordSaveDTO.getAbnormalRemark());
        }
        record.setAbnormalTime(new Date());
        if (StrUtil.isNotBlank(recordSaveDTO.getAbnormalImg())) {
            record.setAbnormalImg(recordSaveDTO.getAbnormalImg());
        }
        record.setAbnormalTime(new Date());
        record.setTriggerMan(Objects.requireNonNull(UserUtils.getCurrentUser()).getUserName());
        record.setTriggerTime(new Date());

        String stationBo = recordSaveDTO.getStationBo();
        record.setStationBo(stationBo);
        String productionBo = andonTriggerMapper.getProductLineBo(stationBo);
        String workShopBo = andonTriggerMapper.getWorkShopBo(productionBo);
        record.setProductLineBo(productionBo);
        record.setWorkShopBo(workShopBo);
        // 安灯报障-维修单号
        ResponseData<String> stringResponseData = codeRuleService.generatorNextNumber("SB_WX");
        if (!ResultResponseEnum.SUCCESS.getCode().equals(stringResponseData.getCode())) {
            throw new CommonException(stringResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        String data = stringResponseData.getData();
        record.setRepairNo(data);

        // 根据什么类型的安灯，选择不同的模板，和组装参数
        String templateCode = "";
        String station = UserUtils.getStation();
        // paramMap 是将模板的变量替换
        Map<String, String> paramMap = new HashMap<>();
        record.setResourceType(andon.getAndonTypeBo());
        String device = "";
        if (StrUtil.isNotBlank(record.getDeviceBo())) {
            List<Map<String, String>> deviceList = andonTriggerMapper.getDeviceList(stationBo);
            if (CollUtil.isEmpty(deviceList)) {
                throw new RuntimeException("当前工位Bo: " + stationBo + " 没有绑定设备");
            }
            record.setDeviceBo(deviceList.get(0).get("deviceBo"));
            device = deviceList.get(0).get("device");
            /* 设备发送 */
            paramMap.put("station", station);
            paramMap.put("device", device);
            //组装参数发送站内信信息，如需推送其余信息，需自行拓展
            Set<AndonTriggerPushUserVo> userVos = andonTriggerMapper.getPushUser(recordSaveDTO.getAndonBo());
            for (AndonTriggerPushUserVo userVo : userVos) {
                if (ObjectUtil.isNotEmpty(userVo)) {
                    Map<String, Object> map = new HashMap<>(4);
                    map.put("code", deviceTemplateCode);
                    map.put("userId", userVo.getUserName());
                    map.put("userName", userVo.getRealName());
                    map.put("params", paramMap);
                    noticeService.sendMessage(map);
                }
            }
        }
        record.setFaultCodeBo(recordSaveDTO.getFaultCodeBo());
        record.setFaultCode(recordSaveDTO.getFaultCode());

        String item = "";
        if (StrUtil.isNotBlank(recordSaveDTO.getItemBo())) {
            record.setItemBo(recordSaveDTO.getItemBo());
            item = andonTriggerMapper.getItem(recordSaveDTO.getItemBo());
            /* 物料发送 */
            paramMap.put("station", station);
            paramMap.put("item", item);
            //组装参数发送站内信信息，如需推送其余信息，需自行拓展
            Set<AndonTriggerPushUserVo> userVos = andonTriggerMapper.getPushUser(recordSaveDTO.getAndonBo());
            for (AndonTriggerPushUserVo userVo : userVos) {
                if (ObjectUtil.isNotEmpty(userVo)) {
                    Map<String, Object> map = new HashMap<>(4);
                    map.put("code", itemTemplateCode);
                    map.put("userId", userVo.getUserName());
                    map.put("userName", userVo.getRealName());
                    map.put("params", paramMap);
                    noticeService.sendMessage(map);
                }
            }
        }
        record.setCallQuantity(recordSaveDTO.getCallQuantity());

        recordMapper.insert(record);

        return data;
    }

    @Override
    public String upload(MultipartFile[] files) {

        String respFileName = "";
        for (int i = 0; i < files.length; i++) {
            String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            String yyyyMMdd = uploadPath + currentDate + File.separator;
            if (FileUtil.exist(yyyyMMdd)) {
                FileUtil.mkdir(yyyyMMdd);
            }
            try {
                File file = FileUtil.writeBytes(files[i].getBytes(), yyyyMMdd + File.separator + UUID.randomUUID() + "@" + files[i].getOriginalFilename());
                if (StrUtil.isNotBlank(file.getName())) {
                    respFileName = file.getAbsolutePath();
                } else {
                    respFileName = "," + file.getAbsolutePath();
                }
            } catch (Exception e) {
                throw new CustomException(CommonCode.FILE_UPLOAD_FAIL);
            }

        }
        return respFileName;
    }

    @Autowired
    private UserService userService;

    @Override
    public RecordVo getRecord(String andonBo) {

        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("state", "1");
        queryWrapper.eq("andon_bo", andonBo);
        Integer count = recordMapper.selectCount(queryWrapper);
        RecordVo recordVo = null;

        if (count == 1) {
            recordVo = recordMapper.getRevord(andonBo);
            if (StrUtil.isNotBlank(recordVo.getAbnormalImg())) {
                recordVo.setImgs(CommonUtils.convertImgToStringList(recordVo.getAbnormalImg()));
            }
        }

        if (null != recordVo) {
            // 用户账号 多个账号以,号拼接
            String callMan = recordVo.getCallMan();
            if (StrUtil.isNotBlank(callMan)) {
                List<String> userNames = Stream.of(callMan.split(",")).collect(Collectors.toList());
                // feign获取用户信息
                ResponseData<List<IapSysUserT>> user = userService.getUserList(userNames);
                List<IapSysUserT> data = user.getData();
                String callManName = data.stream().map(IapSysUserT::getRealName).collect(Collectors.joining(","));
                recordVo.setCallManName(callManName);
            }
        }
        return recordVo;
    }

    @Override
    public Boolean saveRepairCallBack(AndonSaveRepairCallBackDTO andonSaveRepairCallBackDTO) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("andon_bo", andonSaveRepairCallBackDTO.getAndonBo());
        queryWrapper.eq("state", Constant.recordState.TRIGGER.getValue());
        Record record = recordMapper.selectOne(queryWrapper);

        Assert.valid(record == null, "未找到安灯报障记录");
        //保存维修单号
        Record update = new Record();
        update.setPid(record.getPid());
        update.setRepairNo(andonSaveRepairCallBackDTO.getRepairNo());
        return recordMapper.updateById(update) > 0;
    }
}
