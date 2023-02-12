package com.itl.mes.core.provider.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.mes.core.api.dto.DeviceCountStatisticsDTO;
import com.itl.mes.core.api.dto.DeviceDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.api.vo.DevicePlusVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备表 Mapper 接口
 * </p>
 *
 * @author space
 * @since 2019-06-17
 */

public interface DeviceMapper extends BaseMapper<Device> {

    /**
     * 查询可分配设备数据
     *
     * @param site
     * @param deviceBo
     * @return
     */
    List<Map<String, Object>> getAvailableDeviceTypeList(@Param("site") String site, @Param("deviceBo") String deviceBo);

    List<Map<String, Object>> getAssignedDeviceTypeList(@Param("site") String site, @Param("deviceBo") String deviceBo);

    List<Device> selectTop(@Param("site") String site);


    /**
     * 查询机台车间
     *
     * @param page
     * @param deviceDto
     * @return
     */
    IPage<Device> selectDeviceWorkshop(Page page, @Param("device") DeviceDto deviceDto);

    IPage<Map<String, String>> getScrewByLine(Page page, @Param("params") Map<String, Object> params);

    void updateState(@Param("code") String code, @Param("state") String state);

    /**
     * 统计各个状态的设备数量
     *
     * @param site 工厂id
     * @return
     */
    List<DeviceCountStatisticsDTO> queryDeviceCountStatisticsByState(String site);

    List<DevicePlusVo> getDataByCondition(@Param(Constants.WRAPPER) Wrapper wrapper);


}