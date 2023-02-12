package com.itl.iap.mes.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.mes.api.dto.sparepart.DeviceChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.DeviceInfoDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartDeviceMapping;

import java.util.List;
import java.util.Map;

/**
 * 备件-设备关联关系
 * @author dengou
 * @date 2021/9/18
 */
public interface SparePartDeviceMappingService extends IService<SparePartDeviceMapping> {

    /**
     * 保存关联关系， 覆盖原有的
     * @param sparePartId 备件id
     * @param deviceIds 设备id列表
     * */
    Boolean save(String sparePartId, List<String> deviceIds);

    /**
     * 根据备件id查询
     * @param sparePartId 备件id
     * @return list
     * */
    List<SparePartDeviceMapping> listBySparePartId(String sparePartId);
    /**
     * 根据备件id查询关联设备列表
     * @param sparePartId 备件id
     * @return deviceIds
     * */
    List<String> queryDeviceIdsBySparePartId(String sparePartId);

    /**
     * 根据设备id删除关联设备列表
     * @param sparePartId 备件id
     * */
    Boolean removeBySparePartId(String sparePartId);

    /**
     * 查询关联设备列表
     * @param params sparePartId 备件id
     * @return 关联设备列表
     * */
    Page<DeviceInfoDTO> getDeviceList(Map<String, Object> params);

    /**
     * 更换记录分页查询
     * @param params sparePartId 备件id
     * @return 更换记录信息列表
     * */
    Page<DeviceChangeRecordDTO> changeRecord(Map<String, Object> params);

}
