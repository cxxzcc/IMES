package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.PackLevel;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.PackingServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author zhongfei
 * @date 2021/04/26
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "packing", fallback = PackingServiceImpl.class, configuration = FallBackConfig.class)
public interface PackingService {

    @GetMapping("iap_sys_organization_position/packLevel")
    @ApiOperation(value = "查询包装明细BO获取包装对应明细记录")
    ResponseData<PackLevel> getPackLevelByBo(@RequestParam("bo") String bo);
}
