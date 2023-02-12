package com.itl.mes.core.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.dto.MesFilesVO;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;
import com.itl.iap.common.base.mapper.QueryWrapperUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.response.ResultResponseEnum;
import com.itl.iap.common.base.utils.*;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.client.service.FileUploadService;
import com.itl.mes.core.api.bo.DeviceHandleBO;
import com.itl.mes.core.api.bo.ProductLineHandleBO;
import com.itl.mes.core.api.bo.WorkShopHandleBO;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.*;
import com.itl.mes.core.api.entity.*;
import com.itl.mes.core.api.service.CustomDataValService;
import com.itl.mes.core.api.service.DeviceService;
import com.itl.mes.core.api.service.DeviceTypeItemService;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.api.vo.DeviceTypeSimplifyVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.provider.feign.SupplierFeignService;
import com.itl.mes.core.provider.mapper.DeviceMapper;
import com.itl.mes.core.provider.mapper.DeviceTypeMapper;
import com.itl.mes.core.provider.mapper.TInstrumentTypeMapper;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备表 服务实现类
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */
@Service
@Transactional
public class DeviceServiceImpl extends ServiceImpl<DeviceMapper, Device> implements DeviceService {


    @Autowired
    private DeviceMapper deviceMapper;

    @Autowired
    private DeviceTypeMapper deviceTypeMapper;
    @Autowired
    private DeviceTypeItemService deviceTypeItemService;

    @Autowired
    private CustomDataValService customDataValService;

    @Autowired
    private ProductLineServiceImpl productLineService;

    @Autowired
    private StationServiceImpl stationService;

    @Resource
    private UserUtil userUtil;

    //    @Autowired
//    private FileUploadService fileUploadService;
    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private SupplierFeignService supplierFeignService;

    @Resource
    private TInstrumentTypeMapper tInstrumentTypeMapper;


    @Override
    public void updateState(String bo, String state) throws CommonException {
        deviceMapper.updateState(bo, state);
    }

    @Override
    public IPage<Device> selectDeviceWorkshop(
            DeviceDto deviceDto) {
        if (ObjectUtil.isEmpty(deviceDto.getPage())) {
            deviceDto.setPage(new Page(0, 10));
        }
        if (StringUtils.isNotEmpty(deviceDto.getWorkShop())) {
            deviceDto.setWorkShop(new WorkShopHandleBO(UserUtils.getSite(), deviceDto.getWorkShop()).getBo());
        }
        deviceDto.setSite(UserUtils.getSite());
        return deviceMapper.selectDeviceWorkshop(deviceDto.getPage(), deviceDto);
    }

    @Override
    public IPage getScrewByLine(Map<String, Object> params) {
        Page page = new Page(Integer.valueOf(params.get("page").toString()), Integer.valueOf(params.get("pageSize").toString()));
        IPage screwByLine = deviceMapper.getScrewByLine(page, params);
        return screwByLine;
    }

    @Override
    public Integer getDeviceCountBySite(String site) {
        if (StrUtil.isBlank(site)) {
            return 0;
        }
        return lambdaQuery().eq(Device::getSite, site).count();
    }

    @Override
    public Boolean changeState(DeviceChangeStateRequestVO deviceChangeStateRequestVO) throws CommonException {
        String state = deviceChangeStateRequestVO.getState();
        String code = deviceChangeStateRequestVO.getDeviceNo();
        DeviceStateEnum deviceStateEnum = DeviceStateEnum.parseByCode(state);
        if (deviceStateEnum == null) {
            throw new CommonException("状态参数不合法", 999);
        }
        Device device = lambdaQuery().eq(Device::getDevice, code).eq(Device::getSite, UserUtils.getSite()).one();
        if (device == null) {
            throw new CommonException("未找到设备", 999);
        }
        List<String> canChangeStateList = new ArrayList<>();
        canChangeStateList.add(DeviceStateEnum.ZY.getCode());
        canChangeStateList.add(DeviceStateEnum.DY.getCode());
        canChangeStateList.add(DeviceStateEnum.TY.getCode());
        //判断当前状态是否为 在用，待用，停用
        if (!CollUtil.contains(canChangeStateList, device.getState())) {
            throw new CommonException("该设备目前不可变更状态", 999);
        }
        //判断修改状态是否为可变更状态
        if (!CollUtil.contains(canChangeStateList, state)) {
            throw new CommonException("该设备目前不可变更为该状态", 999);
        }
        //判断状态是否有变更
        if (StrUtil.equals(state, device.getState())) {
            return false;
        }
        updateState(new DeviceHandleBO(UserUtils.getSite(), code).getBo(), state);
        return true;
    }

    @Override
    public List<DeviceVo> queryNameByBos(List<String> bos) {
        if (CollUtil.isEmpty(bos)) {
            return Collections.emptyList();
        }
        List<Device> deviceList = lambdaQuery().select(Device::getBo, Device::getDeviceName).in(Device::getBo, bos).list();
        if (CollUtil.isEmpty(deviceList)) {
            return Collections.emptyList();
        }
        List<DeviceVo> list = new ArrayList<>();
        for (Device device : deviceList) {
            DeviceVo deviceVo = new DeviceVo();
            deviceVo.setBo(device.getBo());
            deviceVo.setDeviceName(device.getDeviceName());
            list.add(deviceVo);
        }

        return list;
    }

    /**
     * 验证设备数据是否合规
     *
     * @param device
     * @throws CommonException
     */
    void validateDevice(Device device) throws CommonException {

        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(device);
        if (validResult.hasErrors()) {
            throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
        }

    }

    /**
     * 验证设备指定属性数据格式是否合规
     *
     * @param device
     * @param fields
     * @throws CommonException
     */
    void validateDeviceFields(Device device, String... fields) throws CommonException {
        ValidationUtil.ValidResult validResult = null;
        for (int i = 0; i < fields.length; i++) {
            validResult = ValidationUtil.validateProperty(device, fields[i]);
            if (validResult.hasErrors()) {
                throw new CommonException(validResult.getErrors(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
        }
    }

    @Override
    public List<Device> selectList() {
        QueryWrapper<Device> entityWrapper = new QueryWrapper<>();
        //getEntityWrapper(entityWrapper, device);
        return super.list(entityWrapper);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    public void saveDevice(DeviceVo deviceVo) throws CommonException {
        String site = UserUtils.getSite(); //获取工厂

        String bo = null;
        //设置封面图
        deviceVo.setCoverImg("");
        Map<String, List<MesFilesVO>> mesFiles = deviceVo.getMesFiles();
        if (CollUtil.isNotEmpty(mesFiles)) {
            List<MesFilesVO> pics = mesFiles.get("pics");
            if (CollUtil.isNotEmpty(pics)) {
                deviceVo.setCoverImg(pics.get(0).getFilePath());
            }
        }

        //如果是基准仪器,将其他同类型的仪器修改非基准
        String instrumentTypeId = deviceVo.getInstrumentTypeId();
        if ("1".equals(deviceVo.getIsStandard()) && StrUtil.isNotBlank(instrumentTypeId)) {
            tInstrumentTypeMapper.updateStandard(instrumentTypeId);
        }

        if (StringUtils.isBlank(deviceVo.getBo())) {
            //新增, 状态默认为待用
            deviceVo.setState(DeviceStateEnum.DY.getCode());
            bo = insertDevice(deviceVo);
        } else {
            DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, deviceVo.getDevice());
            Device deviceEntity = super.getById(deviceHandleBO.getBo());//查询设备数据
            bo = deviceHandleBO.getBo();
            //修改, 不更新状态
            deviceVo.setState(null);
            updateDevice(deviceEntity, deviceVo);
        }


        deviceTypeItemService.save(bo, deviceVo.getAssignedDeviceTypeList());

        /* 与文件建立联系 */
        Map<String, List<MesFilesVO>> files = deviceVo.getMesFiles();
        if (files != null && !files.isEmpty()) {
            List<MesFilesVO> pics = files.get("pics");
            List<MesFilesVO> docs = files.get("docs");
            List<MesFilesVO> list = new ArrayList<>();
            if (pics != null && pics.size() > 0) {
                pics.forEach(x -> x.setFileType("pic"));
                list.addAll(pics);
            }
            if (docs != null && docs.size() > 0) {
                docs.forEach(x -> x.setFileType("doc"));
                list.addAll(docs);
            }
            ResponseData resp = fileUploadService.saveFilesForFeign(list, bo);
            if (!ResultResponseEnum.SUCCESS.getCode().equals(resp.getCode())) {
                throw new CommonException(resp.getMsg(), CommonExceptionDefinition.BASIC_EXCEPTION);
            }
        }


        //保存自定义数据
        if (CollUtil.isNotEmpty(deviceVo.getCustomDataValVoList())) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(bo);
            customDataValRequest.setSite(site);
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.DEVICE.getDataType());
            customDataValRequest.setCustomDataValVoList(deviceVo.getCustomDataValVoList());
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }

    private void updateDevice(Device deviceEntity, DeviceVo deviceVo) throws CommonException {
        Date frontModifyDate = deviceVo.getModifyDate(); //前台传递的时间值
        CommonUtil.compareDateSame(frontModifyDate, deviceEntity.getModifyDate()); //比较时间是否相等

        Date newModifyDate = new Date();
        String site = UserUtils.getSite();
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, deviceVo.getDevice());
        Device device = new Device();
        device.setScrewCombination(deviceVo.getScrewCombination());
        device.setBo(deviceHandleBO.getBo());
        device.setSite(site);
        device.setDevice(deviceVo.getDevice());
        device.setDeviceName(deviceVo.getDeviceName());
        device.setDeviceDesc(deviceVo.getDeviceDesc());
        device.setDeviceModel(deviceVo.getDeviceModel());
        device.setState(deviceVo.getState());
        device.setIsProcessDevice(deviceVo.getIsProcessDevice());
        device.setIncharge(deviceVo.getIncharge());
        device.setTel(deviceVo.getTel());
        device.setCoverImg(deviceVo.getCoverImg());
        device.setUsefulLife(deviceVo.getUsefulLife());

        if (!StrUtil.isBlank(deviceVo.getProductLine())) {
            device.setProductLineBo(productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(site, deviceVo.getProductLine())).getBo());
        }
        if (!StrUtil.isBlank(deviceVo.getStation())) {
            device.setStationBo(stationService.selectByStation(deviceVo.getStation()).getBo());
        }
        device.setLocation(deviceVo.getLocation());
        device.setAssetsCode(deviceVo.getAssetsCode());
        device.setManufacturer(deviceVo.getManufacturer());
        device.setValidStartDate(deviceVo.getValidStartDate());
        device.setValidEndDate(deviceVo.getValidEndDate());
        device.setMemo(deviceVo.getMemo());
        device.setModifyUser(userUtil.getUser().getUserName());
        device.setModifyDate(newModifyDate);
        device.setBuyDate(deviceVo.getBuyDate());
        device.setSupplier(deviceVo.getSupplier());

        device.setBaseId(deviceVo.getBaseId());
        device.setBaseLocation(deviceVo.getBaseLocation());
        device.setIsStandard(deviceVo.getIsStandard());
        device.setInstrumentTypeId(deviceVo.getInstrumentTypeId());
        validateDeviceFields(device, "deviceName", "deviceDesc", "deviceModel", "state", "isProcessDevice");
        boolean successFlag = super.updateById(device);
        if (!successFlag) {
            throw new CommonException("设备编号" + deviceVo.getDevice() + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
    }

    private String insertDevice(DeviceVo deviceVo) throws CommonException {
        String site = UserUtils.getSite();
        Device byId = getById(new DeviceHandleBO(site, deviceVo.getDevice()).getBo());
        if (byId != null) {
            throw new CommonException("设备编号已存在", CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
        Device device = new Device();
        device.setScrewCombination(deviceVo.getScrewCombination());
        device.setSite(site);
        device.setDevice(deviceVo.getDevice());

        if (StrUtil.isBlank(device.getDevice())) {
            ResponseData<String> stringResponseData = codeRuleService.generatorNextNumber("SBBH");
            if (!ResultResponseEnum.SUCCESS.getCode().equals(stringResponseData.getCode())) {
                throw new CommonException(stringResponseData.getMsg(), CommonExceptionDefinition.VERIFY_EXCEPTION);
            }
            device.setDevice(stringResponseData.getData());
        }
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(site, device.getDevice());
        device.setBo(deviceHandleBO.getBo());
        device.setDeviceName(deviceVo.getDeviceName());
        device.setDeviceDesc(deviceVo.getDeviceDesc());
        device.setDeviceModel(deviceVo.getDeviceModel());
        device.setState(deviceVo.getState());
        device.setIsProcessDevice(deviceVo.getIsProcessDevice());
        device.setIncharge(deviceVo.getIncharge());
        device.setTel(deviceVo.getTel());
        device.setCoverImg(deviceVo.getCoverImg());
        device.setUsefulLife(deviceVo.getUsefulLife());
        if (!StrUtil.isBlank(deviceVo.getProductLine())) {
            device.setProductLineBo(productLineService.getExistProductLineByHandleBO(new ProductLineHandleBO(site, deviceVo.getProductLine())).getBo());
        }
        if (!StrUtil.isBlank(deviceVo.getStation())) {
            device.setStationBo(stationService.selectByStation(deviceVo.getStation()).getBo());
        }
        device.setLocation(deviceVo.getLocation());
        device.setAssetsCode(deviceVo.getAssetsCode());
        device.setManufacturer(deviceVo.getManufacturer());
        device.setValidStartDate(deviceVo.getValidStartDate());
        device.setValidEndDate(deviceVo.getValidEndDate());
        device.setMemo(deviceVo.getMemo());
        device.setObjectSetBasicAttribute(userUtil.getUser().getUserName(), new Date());
        device.setBuyDate(deviceVo.getBuyDate());
        device.setSupplier(deviceVo.getSupplier());

        device.setBaseId(deviceVo.getBaseId());
        device.setBaseLocation(deviceVo.getBaseLocation());
        device.setIsStandard(deviceVo.getIsStandard());
        device.setInstrumentTypeId(deviceVo.getInstrumentTypeId());
        validateDevice(device);
        deviceMapper.insert(device);
        return device.getBo();
    }

    //通过 device 设备编号查询
    @Override
    public DeviceVo getDeviceVoByDevice(String device) throws CommonException {
        Device deviceEntity = selectByDevice(device);
        String site = UserUtils.getSite();
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(site, deviceEntity.getBo(), CustomDataTypeEnum.DEVICE.getDataType());
        DeviceVo deviceVo = new DeviceVo();
        BeanUtils.copyProperties(deviceEntity, deviceVo);
        deviceVo.setCustomDataAndValVoList(customDataAndValVos);
        List<Map<String, Object>> availableDeviceTypeList = deviceMapper.getAvailableDeviceTypeList(site, deviceEntity.getBo());
        List<DeviceTypeSimplifyVo> available = new ArrayList<>();
        if (!availableDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : availableDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                available.add(deviceTypeSimplifyVo);
            }
        }
        List<Map<String, Object>> assignedDeviceTypeList = deviceMapper.getAssignedDeviceTypeList(site, deviceEntity.getBo());
        List<DeviceTypeSimplifyVo> assigned = new ArrayList<>();
        if (!assignedDeviceTypeList.isEmpty()) {
            for (Map<String, Object> map : assignedDeviceTypeList) {
                DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
                deviceTypeSimplifyVo.setDeviceType(map.get("DEVICE_TYPE").toString());
                deviceTypeSimplifyVo.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                deviceTypeSimplifyVo.setDeviceTypeDesc(map.get("DEVICE_TYPE_DESC").toString());
                assigned.add(deviceTypeSimplifyVo);
            }
        }
        deviceVo.setAssignedDeviceTypeList(assigned);
        deviceVo.setAvailableDeviceTypeList(available);
        deviceVo.setDevice(deviceEntity.getDevice());
        deviceVo.setDeviceName(deviceEntity.getDeviceName());
        deviceVo.setDeviceDesc(deviceEntity.getDeviceDesc());
        deviceVo.setDeviceModel(deviceEntity.getDeviceModel());
        deviceVo.setState(deviceEntity.getState());
        deviceVo.setStateDesc(DeviceStateEnum.parseDescByCode(deviceEntity.getState()));
        deviceVo.setIsProcessDevice(deviceEntity.getIsProcessDevice());
        deviceVo.setIncharge(deviceEntity.getIncharge());
        deviceVo.setTel(deviceEntity.getTel());
        deviceVo.setUsefulLife(deviceEntity.getUsefulLife());
        deviceVo.setBuyDate(deviceEntity.getBuyDate());
        deviceVo.setBo(deviceEntity.getBo());
        deviceVo.setBaseId(deviceEntity.getBaseId());
        deviceVo.setBaseLocation(deviceEntity.getBaseLocation());
        deviceVo.setIsStandard(deviceEntity.getIsStandard());
        String instrumentTypeId = deviceEntity.getInstrumentTypeId();
        //仪器类型
        if (StrUtil.isNotBlank(instrumentTypeId)) {
            deviceVo.setInstrumentTypeId(instrumentTypeId);
            TInstrumentType tInstrumentType = tInstrumentTypeMapper.selectById(instrumentTypeId);
            deviceVo.setInstrumentTypeName(tInstrumentType.getName());
        }

        //供应商信息
        if (StrUtil.isNotBlank(deviceVo.getSupplier())) {
            ResponseData<Map<String, Object>> result = supplierFeignService.getById(deviceVo.getSupplier());
            if (result.isSuccess()) {
                Map<String, Object> data = result.getData();
                deviceVo.setSupplierName((String) data.getOrDefault("vendName", ""));
            }
        }

        /* 获取文件列表 */
        ResponseData<List<MesFiles>> resp = fileUploadService.getFileInfo(new DeviceHandleBO(site, device).getBo());
        if (ResultResponseEnum.SUCCESS.getCode().equals(resp.getCode())) {
            List<MesFiles> list = resp.getData();
            if (list != null && list.size() > 0) {
                List<MesFilesVO> mesFilesVOS = new ArrayList<>();
                for (MesFiles mesFiles : list) {
                    MesFilesVO item = new MesFilesVO();
                    BeanUtils.copyProperties(mesFiles, item);
                    mesFilesVOS.add(item);
                }
                Map<String, List<MesFilesVO>> listMap = mesFilesVOS.stream().collect(Collectors.groupingBy(MesFilesVO::getFileType));
                Map<String, List<MesFilesVO>> map = new HashMap<>(5);
                map.put("pics", listMap.get("pic"));
                map.put("docs", listMap.get("doc"));
                deviceVo.setMesFiles(map);
            }
        }

        if (!StrUtil.isBlank(deviceEntity.getProductLineBo())) {
            deviceVo.setProductLine(split(deviceEntity.getProductLineBo()));
        }
        if (!StrUtil.isBlank(deviceEntity.getStationBo())) {
            deviceVo.setStation(split(deviceEntity.getStationBo()));
        }
        deviceVo.setLocation(deviceEntity.getLocation());
        deviceVo.setAssetsCode(deviceEntity.getAssetsCode());
        deviceVo.setManufacturer(deviceEntity.getManufacturer());
        deviceVo.setValidStartDate(deviceEntity.getValidStartDate());
        deviceVo.setValidEndDate(deviceEntity.getValidEndDate());
        deviceVo.setMemo(deviceEntity.getMemo());
        deviceVo.setModifyDate(deviceEntity.getModifyDate());
        return deviceVo;
    }

    @Override
    public List<Device> selectByDevices(List<String> devices) throws CommonException {
        List<String> bos = new ArrayList<>();
        for (String device : devices) {
            DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
            bos.add(deviceHandleBO.getBo());
        }
        QueryWrapper<Device> queryWrapper = new QueryWrapper();
        queryWrapper.lambda().in(Device::getBo, bos);
        List<Device> deviceEntityList = deviceMapper.selectList(queryWrapper);
        return deviceEntityList;
    }

    //删除
    @SneakyThrows
    @Override
    public void deleteDevice(String device, Date modifyDate) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
        Device deviceEntity = deviceMapper.selectById(deviceHandleBO.getBo());
        if (deviceEntity == null) {
            throw new CommonException("设备" + device + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        CommonUtil.compareDateSame(modifyDate, deviceEntity.getModifyDate());
        deviceMapper.deleteById(deviceEntity.getBo());
        deviceTypeItemService.deleteByDeviceBO(deviceEntity.getBo());
    }

    //查询500条设备类型数据供页面初始化使用
    @Override
    public IPage<DeviceType> getDeviceTypeVoList(Page page) {
        if (page == null) {
            page = new Page(1, 10);
        }
        IPage<DeviceType> deviceTypes = deviceTypeMapper.selectTop(page, UserUtils.getSite());

        return deviceTypes;
    }

    public Device selectByDevice(String device) throws CommonException {
        DeviceHandleBO deviceHandleBO = new DeviceHandleBO(UserUtils.getSite(), device);
        Device deviceEntity = deviceMapper.selectById(deviceHandleBO.getBo());
        if (deviceEntity == null) {
            throw new CommonException("设备编号:" + device + "未维护", CommonExceptionDefinition.BASIC_EXCEPTION);
        }
        return deviceEntity;
    }

    //切割BO 获取 第二个值
    public String split(String bo) {
        String[] split = bo.split(",");
        return split[1];
    }

    @Override
    public List<DeviceCountStatisticsDTO> queryDeviceCountStatisticsByState(String site) {
        List<DeviceCountStatisticsDTO> list = baseMapper.queryDeviceCountStatisticsByState(site);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        list.forEach(e -> {
            e.setStateDesc(DeviceStateEnum.parseDescByCode(e.getState()));
        });
        return list;
    }


    @Value("${file.path}")
    private String filePath;


    @Override
    public void export(HttpServletRequest request, HttpServletResponse response) {
        String site = UserUtils.getSite();
        QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("site", UserUtils.getSite()).orderByDesc("CREATE_DATE");
        List<Device> devices = deviceMapper.selectList(queryWrapper);
        //优化处理错误
        List<String> collect = devices.stream().filter(v -> StrUtil.isNotBlank(v.getSupplier())).map(Device::getSupplier).distinct().collect(Collectors.toList());
        Map<String, Map<String, Object>> mm = new HashMap<>();
        if (CollectionUtil.isNotEmpty(collect)) {
            ResponseData<List<Map<String, Object>>> result = supplierFeignService.getByIds(collect);
            if (result.isSuccess()) {
                List<Map<String, Object>> data = result.getData();
                mm = data.stream().collect(Collectors.toMap(v -> (String) v.get("id"), Function.identity()));
            }
        }

        for (Device device : devices) {
//            ResponseData<Map<String, Object>> result = supplierFeignService.getById(device.getSupplier());
//            if (result.isSuccess()) {
//                Map<String, Object> data = result.getData();
//                device.setSupplierName((String) data.getOrDefault("vendName", ""));
//            }
            if (mm.containsKey(device.getSupplier())) {
                Map<String, Object> stringObjectMap = mm.get(device.getSupplier());
                device.setSupplierName((String) stringObjectMap.getOrDefault("supplierName", ""));
            }

            List<Map<String, Object>> availableDeviceTypeList = deviceMapper.getAvailableDeviceTypeList(site, device.getBo());
            if (!availableDeviceTypeList.isEmpty()) {
                for (Map<String, Object> map : availableDeviceTypeList) {
                    device.setDeviceTypeName(map.get("DEVICE_TYPE_NAME").toString());
                }
            }
            String stateDesc = DeviceStateEnum.parseDescByCode(device.getState());
            device.setStatedesc(stateDesc);
            ProductLine productLineVo = productLineService.getById(device.getProductLineBo());
            if (productLineVo != null) {
                device.setProductLine(productLineVo.getProductLineDesc());
            }
            Station stationVo = stationService.getById(device.getStationBo());
            if (stationVo != null) {
                device.setStation(stationVo.getStationName());
            }
//            devices.add(device);
//            break;
        }
        ExcelUtils.exportExcel(devices, "设备列表", "设备列表", Device.class, "设备列表.xls", true, response);

    }

    @Override
    public List<DevicePlusVo> getDataByCondition(DeviceConditionDto deviceLikeDto) {
        QueryWrapper queryWrapper = QueryWrapperUtil.convertQuery(deviceLikeDto);
        String diviceCodeOrName = deviceLikeDto.getDiviceCodeOrName();
        if (StrUtil.isNotBlank(diviceCodeOrName)) {
            //待封装
//            diviceCodeOrName = diviceCodeOrName.replace("*", "%");
            queryWrapper.apply("( device like CONCAT('%',{0},'%') or device_name like CONCAT('%',{1},'%'))", diviceCodeOrName, diviceCodeOrName);
        }
        List<DevicePlusVo> list = deviceMapper.getDataByCondition(queryWrapper);
        return list;
    }


    public boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    @Override
    public void saveByImport(List<DeviceVo> list) {
        for (int i = 0; i < list.size(); i++) {
            DeviceVo deviceVo = list.get(i);
            DeviceType deviceType = deviceTypeMapper.getByDeviceType(deviceVo.getDeviceType());
            List<DeviceTypeSimplifyVo> assignedDeviceTypeList = new ArrayList<>();
            DeviceTypeSimplifyVo deviceTypeSimplifyVo = new DeviceTypeSimplifyVo();
            deviceTypeSimplifyVo.setDeviceTypeName(deviceType.getDeviceTypeName());
            deviceTypeSimplifyVo.setDeviceType(deviceType.getDeviceType());
            deviceTypeSimplifyVo.setDeviceTypeDesc(deviceType.getDeviceType());
            assignedDeviceTypeList.add(deviceTypeSimplifyVo);
            String bo = null;
            if (StringUtils.isBlank(deviceVo.getBo())) {
                //新增, 状态默认为待用
                deviceVo.setState(DeviceStateEnum.DY.getCode());
                bo = insertDevice(deviceVo);
            }
            deviceTypeItemService.save(bo, assignedDeviceTypeList);
        }
    }
}


