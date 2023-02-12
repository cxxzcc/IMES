package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.attachment.client.service.DictService;
import com.itl.iap.common.base.constants.SystemDictCodeConstant;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.iap.common.base.utils.Assert;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.common.base.utils.UserUtils;
import com.itl.iap.mes.api.dto.sparepart.SparePartDTO;
import com.itl.iap.mes.api.entity.MesFiles;
import com.itl.iap.mes.api.entity.sparepart.SparePart;
import com.itl.iap.mes.api.entity.sparepart.SparePartInventory;
import com.itl.iap.mes.api.service.SparePartDeviceMappingService;
import com.itl.iap.mes.api.service.SparePartInventoryService;
import com.itl.iap.mes.api.service.SparePartService;
import com.itl.iap.mes.provider.feign.SupplierFeignService;
import com.itl.iap.mes.provider.mapper.SparePartMapper;
import com.itl.mes.core.api.constant.CustomDataTypeEnum;
import com.itl.mes.core.api.dto.CustomDataValRequest;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.service.CodeRuleService;
import com.itl.mes.core.client.service.CustomDataValService;
import com.itl.mes.core.client.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 备件业务实现
 * @author dengou
 * @date 2021/9/17
 */
@Service
public class SparePartServiceImpl extends ServiceImpl<SparePartMapper, SparePart> implements SparePartService {


    @Autowired
    private SparePartDeviceMappingService sparePartDeviceMappingService;
    @Autowired
    private MesFilesServiceImpl mesFilesService;
    @Autowired
    private CustomDataValService customDataValService;
    @Autowired
    private CodeRuleService codeRuleService;
    @Autowired
    private SparePartInventoryService sparePartInventoryService;
    @Autowired
    private SupplierFeignService supplierFeignService;
    @Autowired
    private DictService dictService;
    @Autowired
    private DeviceService deviceService;

    @Override
    public Page<SparePartDTO> page(Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        QueryPage<SparePartDTO> queryPage = new QueryPage<>(params);
        List<SparePartDTO> list = baseMapper.getPage(queryPage, params);
        if(CollUtil.isNotEmpty(list)) {
            //备件类型信息
            ResponseData<Map<String, String>> dictItemMap = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_TYPE);
            if(dictItemMap.isSuccess()) {
                Map<String, String> map = dictItemMap.getData();
                list.forEach(e -> {
                    if(map.containsKey(e.getType())) {
                        e.setTypeDesc(map.get(e.getType()));
                    }
                });
            }
            //供应商信息
            List<String> ids = list.stream().map(SparePartDTO::getSupplier).collect(Collectors.toList());
            if(CollUtil.isNotEmpty(ids)) {
                ResponseData<List<Map<String, Object>>> result = supplierFeignService.getByIds(ids);
                if(result.isSuccess()) {
                    List<Map<String, Object>> data = result.getData();
                    Map<String, String> map = data.stream().collect(Collectors.toMap(e -> (String) e.get("id"), e -> (String) e.get("supplierName")));
                    list.forEach(e -> {
                        if(map.containsKey(e.getSupplier())) {
                            e.setSupplierName(map.get(e.getSupplier()));
                        }
                    });
                }

            }
        }
        queryPage.setRecords(list);
        return queryPage;
    }

    @Override
    public Page<SparePartDTO> pageByInventory(Map<String, Object> params) {
        params.put("site", UserUtils.getSite());
        QueryPage<SparePartDTO> queryPage = new QueryPage<>(params);
        List<SparePartDTO> list = baseMapper.getPageByInventory(queryPage, params);
        if(CollUtil.isNotEmpty(list)) {
            //备件类型信息
            ResponseData<Map<String, String>> dictItemMap = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_TYPE);
            if(dictItemMap.isSuccess()) {
                Map<String, String> map = dictItemMap.getData();
                list.forEach(e -> {
                    if(map.containsKey(e.getType())) {
                        e.setTypeDesc(map.get(e.getType()));
                    }
                });
            }
        }
        queryPage.setRecords(list);
        return queryPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addSparePart(SparePart sparePart) throws CommonException {

        Assert.valid(checkExistsBySparePartNo(sparePart, UserUtils.getSite()), "备件编号已存在");

        Map<String, List<MesFiles>> mesFiles = sparePart.getMesFiles();

        //保存备件信息
        if(CollUtil.isNotEmpty(mesFiles)) {
            List<MesFiles> pics = mesFiles.get("pics");
            if(CollUtil.isNotEmpty(pics)) {
                sparePart.setCoverImg(pics.get(0).getFilePath());
            }
        }
        sparePart.setCreateTime(new Date());
        sparePart.setCreateUser(UserUtils.getUserId());
        sparePart.setSite(UserUtils.getSite());

        boolean save = save(sparePart);
        String sparePartId = sparePart.getId();
        if(save) {
            //保存设备-备件关联关系
            sparePartDeviceMappingService.save(sparePartId, sparePart.getDeviceIdList());

            //保存备件文件关联信息
            saveFiles(mesFiles, sparePartId);

            //保存自定义数据
            saveCustomDataVal(sparePart.getCustomDataAndValVos(), sparePartId);
        }
        return save;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateSparePart(SparePart sparePart) {
        //检查
        SparePart one = lambdaQuery().eq(SparePart::getId, sparePart.getId()).eq(SparePart::getSite, UserUtils.getSite()).one();
        Assert.valid(one == null, "未找到备件信息");

        Assert.valid(checkExistsBySparePartNo(sparePart, UserUtils.getSite()), "备件编号已存在");

        Map<String, List<MesFiles>> mesFiles = sparePart.getMesFiles();

        //保存备件信息
        sparePart.setCoverImg("");
        if(CollUtil.isNotEmpty(mesFiles)) {
            List<MesFiles> pics = mesFiles.get("pics");
            if(CollUtil.isNotEmpty(pics)) {
                sparePart.setCoverImg(pics.get(0).getFilePath());
            }
        }
        sparePart.setUpdateTime(new Date());
        sparePart.setUpdateUser(UserUtils.getUserId());
        boolean save = updateById(sparePart);
        String sparePartId = sparePart.getId();
        if(save) {
            //保存设备-备件关联关系
            sparePartDeviceMappingService.save(sparePartId, sparePart.getDeviceIdList());

            //保存备件文件关联信息
            saveFiles(mesFiles, sparePartId);
            //保存自定义数据
            saveCustomDataVal(sparePart.getCustomDataAndValVos(), sparePartId);
        }
        return save;
    }


    /**
     * 根据设备编号和工厂查询备件是否存在
     * */
    @Override
    public Boolean checkExistsBySparePartNo(SparePart sparePart, String site) {
        LambdaQueryChainWrapper<SparePart> eq = lambdaQuery().eq(SparePart::getSite, site).eq(SparePart::getSparePartNo, sparePart.getSparePartNo());
        if(StrUtil.isNotBlank(sparePart.getId())) {
            eq.ne(SparePart::getId, sparePart.getId());
        }
        return eq.count() > 0;

    }

    @Override
    public SparePart detail(String id) {
        //备件基本信息
        SparePart sparePart = getById(id);
        if(sparePart == null) {
            return null;
        }
        //备件类型信息
        ResponseData<Map<String, String>> dictItemMap = dictService.getDictItemMapByParentCode(SystemDictCodeConstant.SPARE_PART_TYPE);
        if(dictItemMap.isSuccess()) {
            Map<String, String> map = dictItemMap.getData();
            sparePart.setTypeDesc(map.get(sparePart.getType()));
        }
        //供应商信息
        if(StrUtil.isNotBlank(sparePart.getSupplier())) {
            ResponseData<Map<String, Object>> result = supplierFeignService.getById(sparePart.getSupplier());
            if(result.isSuccess()) {
                Map<String, Object> data = result.getData();
                sparePart.setSupplierName((String)data.getOrDefault("vendName", ""));
            }
        }
        //库存信息
        List<SparePartInventory> sparePartInventories = sparePartInventoryService.listBySparePartId(id);
        sparePart.setSparePartInventories(sparePartInventories);
        sparePart.setCurrentInventory(0);
        if(CollUtil.isNotEmpty(sparePartInventories)){
            Integer currentInventory = sparePartInventories.stream().collect(Collectors.summingInt(SparePartInventory::getInventory));
            sparePart.setCurrentInventory(currentInventory);
        }

        //图片，视频信息
        List<MesFiles> mesFiles = mesFilesService.listByGroupId(id);
        if(CollUtil.isNotEmpty(mesFiles)) {
            Map<String, List<MesFiles>> mesFileMap = mesFiles.stream().collect(Collectors.groupingBy(MesFiles::getFileType));
            sparePart.setMesFiles(mesFileMap);
        }
        //关联设备列表_id
        List<String> deviceIds = sparePartDeviceMappingService.queryDeviceIdsBySparePartId(id);
        sparePart.setDeviceIdList(deviceIds);
        //设备名称列表
        ResponseData<List<DeviceVo>> deviceNameByBos = deviceService.getDeviceNameByBos(deviceIds);
        if(deviceNameByBos.isSuccess()) {
            List<DeviceVo> data = deviceNameByBos.getData();
            sparePart.setDeviceVoList(data);
        }
        //自定义列查询
        List<CustomDataAndValVo> customDataAndValVos = customDataValService.selectCustomDataAndValByBoAndDataType(UserUtils.getSite(), id, CustomDataTypeEnum.SPARE_PART.getDataType());
        sparePart.setCustomDataAndValVos(customDataAndValVos);
        return sparePart;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteSparePart(String id) {
        Assert.valid(StrUtil.isBlank(id), "未找到备件信息");
        SparePart one = lambdaQuery().eq(SparePart::getId, id).eq(SparePart::getSite, UserUtils.getSite()).one();
        Assert.valid(one == null, "未找到备件信息");
        boolean b = removeById(id);
        if(b) {
            //删除设备关联
            sparePartDeviceMappingService.removeBySparePartId(id);
            //删除文件信息
            mesFilesService.removeByGroupId(id);
        }
        return b;
    }

    @Override
    public Boolean deleteSparePartBatch(List<String> ids) {
        if(CollUtil.isEmpty(ids)) {
            return false;
        }
        for (String id : ids) {
            deleteSparePart(id);
        }
        return true;
    }

    @Override
    public void saveByImport(List<SparePart> list) {
        for (int i = 0; i < list.size(); i++){
            SparePart sparePart = list.get(i);
            ResponseData<DeviceVo> deviceVo = deviceService.getDeviceVoByDevice(sparePart.getDeviceCode());
            DeviceVo deviceVo1 = deviceVo.getData();
            List<String> deviceIds = new ArrayList<>();
            deviceIds.add(deviceVo1.getBo());
            sparePart.setDeviceIdList(deviceIds);
            addSparePart(sparePart);
        }
    }

    /**
     * 保存文件关联信息
     * */
    private Boolean saveFiles(Map<String, List<MesFiles>> map, String id) {
        if (CollUtil.isEmpty(map)) {
            return true;
        }
        List<MesFiles> pics = map.getOrDefault("pics", Collections.emptyList());
        List<MesFiles> videos = map.getOrDefault("videos", Collections.emptyList());
        List<String> ids = new ArrayList<>();
        List<String> picIds = pics.stream().map(MesFiles::getId).collect(Collectors.toList());
        List<String> videoIds = videos.stream().map(MesFiles::getId).collect(Collectors.toList());
        List<MesFiles> list = mesFilesService.listByGroupId(id);
        ids.addAll(picIds);
        ids.addAll(videoIds);
        if(CollUtil.isNotEmpty(list)) {
            Set<String> idsByGroupId = list.stream().map(MesFiles::getId).collect(Collectors.toSet());
            ids.addAll(idsByGroupId);
        }
        if(CollUtil.isNotEmpty(ids)) {
            mesFilesService.removeByIds(ids);
        }

        List<MesFiles> newFiles = new ArrayList<>();
        for (MesFiles pic : pics) {
            pic.setGroupId(id);
            pic.setFileType("pics");
            newFiles.add(pic);
        }
        for (MesFiles video : videos) {
            video.setGroupId(id);
            video.setFileType("videos");
            newFiles.add(video);
        }
        if(CollUtil.isEmpty(newFiles)) {
            return true;
        }
        return mesFilesService.saveBatch(newFiles);
    }

    private void saveCustomDataVal(List<CustomDataAndValVo> customDataAndValVos, String sparePartId) {
        //保存自定义数据
        if (CollUtil.isNotEmpty(customDataAndValVos)) {
            CustomDataValRequest customDataValRequest = new CustomDataValRequest();
            customDataValRequest.setBo(sparePartId);
            customDataValRequest.setSite(UserUtils.getSite());
            customDataValRequest.setCustomDataType(CustomDataTypeEnum.SPARE_PART.getDataType());
            List<CustomDataValVo> customDataValVos = new ArrayList<>();
            for (CustomDataAndValVo item : customDataAndValVos) {
                CustomDataValVo customDataValVo = new CustomDataValVo();
                customDataValVo.setVals(item.getVals());
                customDataValVo.setAttribute(item.getAttribute());
            }
            customDataValRequest.setCustomDataValVoList(customDataValVos);
            customDataValService.saveCustomDataVal(customDataValRequest);
        }
    }
}
