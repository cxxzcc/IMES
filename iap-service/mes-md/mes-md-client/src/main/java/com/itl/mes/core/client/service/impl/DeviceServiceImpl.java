package com.itl.mes.core.client.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.DeviceConditionDto;
import com.itl.mes.core.api.entity.Device;
import com.itl.mes.core.api.vo.DevicePlusVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.service.DeviceService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2020/12/14
 * @since JDK1.8
 */
@Slf4j
@Component
public class DeviceServiceImpl implements DeviceService, FallbackFactory {

    @Override
    public Object create(Throwable throwable) {
        System.out.printf(throwable.getMessage());
        return new DeviceServiceImpl();
    }

    @GetMapping("/devices/upateState")
    @Override
    public ResponseData<DeviceVo> upateState(String code, String site, String state) {
        log.error("sorry DeviceService upateState feign fallback ");
        return null;
    }

    @Override
    public ResponseData<DeviceVo> getDeviceVoByDevice(String device) {
        log.error("sorry DeviceService getDeviceVoByDevice feign fallback device:{}" + device);
        return ResponseData.error("getDeviceVoByDevice调用失败");
    }

    @Override
    public ResponseData<List<Device>> getDeviceVoByDevices(List<String> device) {
        log.error("sorry DeviceService getDeviceVoByDevices feign fallback device:{}" + device);
        return null;
    }

    @Override
    public ResponseData<Integer> getDeviceCountBySite(String site) {
        log.error("sorry DeviceService getDeviceCountBySite feign fallback device:{}" + site);
        return ResponseData.error("getDeviceCountBySite调用失败");
    }

    @Override
    public ResponseData<List<DeviceVo>> getDeviceNameByBos(List<String> bos) {
        log.error("sorry DeviceService getDeviceNameByBos feign fallback device:{}" + CollUtil.join(bos, ","));
        return ResponseData.error("getDeviceNameByBos调用失败");
    }

    @Override
    public ResponseData<List<DevicePlusVo>> getDataByCondition(DeviceConditionDto deviceLikeDto) {
        log.error("sorry DeviceService getDataByCondition feign fallback device:{}" + JSONUtil.toJsonStr(deviceLikeDto));
        return ResponseData.error("getDataByCondition失败");
    }

    @Override
    public ResponseData<Device> getDeviceById(String id) {
        log.error("sorry DeviceService getDeviceById feign fallback device:{}" + id);
        return ResponseData.error("getDeviceById调用失败");    }
}
