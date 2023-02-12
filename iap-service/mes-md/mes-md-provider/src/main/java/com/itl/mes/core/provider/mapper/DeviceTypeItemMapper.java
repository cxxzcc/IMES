package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.entity.DeviceTypeItem;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 设备类型明细 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */

public interface DeviceTypeItemMapper extends BaseMapper<DeviceTypeItem> {

    List<DeviceType> getByDevice(@Param("deviceBo") String deviceBo);
}
