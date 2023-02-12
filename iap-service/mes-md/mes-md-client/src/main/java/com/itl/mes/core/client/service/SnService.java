package com.itl.mes.core.client.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.Sn;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.SnServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author cch
 * @date 2021/4/27$
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider", contextId = "sn", fallback = SnServiceImpl.class, configuration = FallBackConfig.class)
public interface SnService {


    @PostMapping("/monopy/sns/update/{type}")
    @ApiOperation(value = "变更条码状态type-UP/DOWN")
    ResponseData save(@RequestBody List<String> sns, @PathVariable("type") String type);

    @PostMapping("/monopy/sns/check/{sn}/{type}")
    @ApiOperation(value = "校验条码type-UP/DOWN")
    ResponseData check(@PathVariable("sn") String sn, @PathVariable("type") String type);

    @GetMapping("/monopy/sns/getSnInfo/{sn}")
    @ApiOperation(value = "getSnInfo for feign")
    Sn getSnInfo(@PathVariable("sn") String sn) throws CommonException;


}
