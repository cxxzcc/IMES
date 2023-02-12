package com.itl.mes.core.client.service;

import com.alibaba.druid.sql.visitor.SQLEvalVisitorUtils;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.client.service.impl.DeviceServiceImpl;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.config.FallBackConfig;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "device", fallbackFactory = DeviceServiceImpl.class, configuration = FallBackConfig.class)
public interface DeviceService {


    /**
     * 修改设备状态
     */
    @GetMapping("/devices/upateState")
    @ApiOperation(value = "修改设备状态")
    ResponseData upateState(@RequestParam String code, @RequestParam String site, @RequestParam String state);

    /**
     * 根据device查询
     *
     * @param device 设备编号
     * @return
     */
    @GetMapping("/devices/query")
    @ApiOperation(value = "通过设备编号查询数据")
    ResponseData<DeviceVo> getDeviceVoByDevice(@RequestParam String device);

    /**
     * 根据device查询
     *
     * @param device 设备编号
     * @return
     */
    @PostMapping("/devices/query/list")
    @ApiOperation(value = "通过设备编号查询数据")
    ResponseData<List<Device>> getDeviceVoByDevices(List<String> device);



    /**
     * 获取所有设备数量
     */
    @GetMapping("/devices/count/{site}")
    ResponseData<Integer> getDeviceCountBySite(@PathVariable("site") String site);


    /**
     * 根据设备bo查询设备名称
     */
    @PostMapping("/devices/queryNames")
    @ApiOperation(value = "通过设备编号查询数据")
    ResponseData<List<DeviceVo>> getDeviceNameByBos(@RequestBody List<String> bos);

    @PostMapping("/devices/getDataByCondition")
    @ApiOperation(value = "通过查询条件带*查询设备")
    ResponseData<List<DevicePlusVo>> getDataByCondition(@RequestBody DeviceConditionDto deviceLikeDto);

    /**
     * 根据id查询
     *
     * @param id 主键
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "通过主键查询数据")
    ResponseData<Device> getDeviceById(@PathVariable String id);
}
