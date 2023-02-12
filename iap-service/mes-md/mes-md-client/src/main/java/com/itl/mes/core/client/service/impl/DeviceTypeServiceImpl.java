package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.DeviceService;
import com.itl.mes.core.client.service.DeviceTypeService;
import feign.hystrix.FallbackFactory;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * 设备类型
 * </p>
 *
 * @author zhancen
 * @since 2019-06-17
 */
@Slf4j
@Component
public class DeviceTypeServiceImpl implements DeviceTypeService, FallbackFactory {



    @Override
    public Object create(Throwable throwable) {
        System.out.printf(throwable.getMessage());
        return new DeviceTypeServiceImpl();
    }


    @Override
    public ResponseData<DeviceTypeVo> getDeviceTypeVoByDeviceType(String deviceType) throws CommonException {
        log.error("sorry DeviceTypeService getDeviceTypeVoByDeviceType feign fallback device:{}" + deviceType);
        return ResponseData.error("getDeviceTypeVoByDeviceType调用失败");
    }
}
