package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.itl.mes.core.api.entity.DeviceType;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备类型表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */

public interface DeviceTypeMapper extends BaseMapper<DeviceType> {

    List<Map<String,Object>> getAvailableDeviceList(@Param("site") String site, @Param("deviceTypeBo") String bo);

    List<Map<String,Object>> getAssignedDeviceList(@Param("site") String site, @Param("deviceTypeBo") String bo);

    IPage<DeviceType> selectTop(IPage page, @Param("site")String site);

    DeviceType getByDeviceType(String deviceType);
}