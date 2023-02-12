package com.itl.iap.mes.provider.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itl.iap.common.base.constants.DeviceStateEnum;
import com.itl.iap.common.base.utils.QueryPage;
import com.itl.iap.mes.api.dto.sparepart.DeviceChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.DeviceInfoDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartDeviceMapping;
import com.itl.iap.mes.api.service.SparePartDeviceMappingService;
import com.itl.iap.mes.provider.mapper.SparePartDeviceMappingMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 备件-设备关联关系crud service
 * @author dengou
 * @date 2021/9/18
 */
@Service
public class SparePartDeviceMappingServiceImpl extends ServiceImpl<SparePartDeviceMappingMapper, SparePartDeviceMapping> implements SparePartDeviceMappingService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean save(String sparePartId, List<String> deviceIds) {
        if(StrUtil.isBlank(sparePartId)) {
            return true;
        }
        List<SparePartDeviceMapping> list = lambdaQuery().eq(SparePartDeviceMapping::getSparePartId, sparePartId).list();
        if(CollUtil.isNotEmpty(list)) {
            Set<String> ids = list.stream().map(SparePartDeviceMapping::getId).collect(Collectors.toSet());
            removeByIds(ids);
        }
        List<SparePartDeviceMapping> sparePartDeviceMappings = new ArrayList<>();
        if(CollUtil.isEmpty(deviceIds)) {
            return true;
        }
        Set<String> deviceIdSet = deviceIds.stream().collect(Collectors.toSet());
        for (String deviceId : deviceIdSet) {
            SparePartDeviceMapping item = SparePartDeviceMapping.builder().deviceId(deviceId).sparePartId(sparePartId).build();
            sparePartDeviceMappings.add(item);
        }
        return saveBatch(sparePartDeviceMappings);
    }

    @Override
    public List<SparePartDeviceMapping> listBySparePartId(String sparePartId) {
        return lambdaQuery().eq(SparePartDeviceMapping::getSparePartId, sparePartId).list();
    }

    @Override
    public List<String> queryDeviceIdsBySparePartId(String sparePartId) {
        List<SparePartDeviceMapping> list = listBySparePartId(sparePartId);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(SparePartDeviceMapping::getDeviceId).distinct().collect(Collectors.toList());
    }

    @Override
    public Boolean removeBySparePartId(String sparePartId) {
        List<SparePartDeviceMapping> list = listBySparePartId(sparePartId);
        if (CollUtil.isEmpty(list)) {
            return true;
        }
        return removeByIds(list.stream().map(SparePartDeviceMapping::getId).collect(Collectors.toSet()));
    }

    @Override
    public Page<DeviceInfoDTO> getDeviceList(Map<String, Object> params) {
        Page<DeviceInfoDTO> page = new QueryPage<>(params);
        List<DeviceInfoDTO> list = baseMapper.getDeviceList(page, (String) params.get("sparePartId"));
        if(CollUtil.isNotEmpty(list)) {
            list.stream().forEach(e -> {
                e.setStatusDesc(DeviceStateEnum.parseDescByCode(e.getStatusCode()));
            });
        }
        page.setRecords(list);
        return page;
    }

    @Override
    public Page<DeviceChangeRecordDTO> changeRecord(Map<String, Object> params) {
        Page<DeviceChangeRecordDTO> page = new QueryPage<>(params);
        List<DeviceChangeRecordDTO> list = baseMapper.changeRecord(page, (String) params.get("sparePartId"));
        page.setRecords(list);
        return page;
    }
}
