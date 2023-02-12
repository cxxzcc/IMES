package com.itl.mes.core.client.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.DeviceType;
import com.itl.mes.core.api.vo.DeviceTypeVo;
import com.itl.mes.core.api.vo.DeviceVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.DeviceServiceImpl;
import com.itl.mes.core.client.service.impl.DeviceTypeServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备类型
 * </p>
 *
 * @author zhancen
 * @since 2019-06-17
 */
@FeignClient(value = "mes-md-provider", contextId = "deviceType", fallbackFactory = DeviceTypeServiceImpl.class, configuration = FallBackConfig.class)
public interface DeviceTypeService {

    /**
     * 根据deviceType查询
     *
     * @param deviceType 设备类型编号
     * @return
     */
    @GetMapping("/deviceTypes/query")
    @ApiOperation(value = "通过设备类型编号查询数据")
   ResponseData<DeviceTypeVo> getDeviceTypeVoByDeviceType(@RequestParam String deviceType) throws CommonException;



}
