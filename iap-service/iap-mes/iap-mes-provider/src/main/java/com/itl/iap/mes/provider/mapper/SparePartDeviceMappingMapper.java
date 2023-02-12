package com.itl.iap.mes.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.mes.api.dto.sparepart.DeviceChangeRecordDTO;
import com.itl.iap.mes.api.dto.sparepart.DeviceInfoDTO;
import com.itl.iap.mes.api.entity.sparepart.SparePartDeviceMapping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 备件-设备关联关系
 * @author dengou
 * @date 2021/9/17
 */
public interface SparePartDeviceMappingMapper extends BaseMapper<SparePartDeviceMapping> {

    /**
     * 查询关联设备列表
     * @param sparePartId 备件id
     * */
    List<DeviceInfoDTO> getDeviceList(Page page, @Param("sparePartId") String sparePartId);

    /**
     * 更换记录分页列表
     * @param sparePartId 备件id
     * */
    List<DeviceChangeRecordDTO> changeRecord(Page<DeviceChangeRecordDTO> page, @Param("sparePartId") String sparePartId);
}
