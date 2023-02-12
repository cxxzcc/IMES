package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.RouterFitDto;
import com.itl.mes.core.api.entity.Router;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.RouterFitServiceImpl;
import com.itl.mes.core.client.service.impl.RouterServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author chenjx1
 * @date 2021/10/20
 */
@FeignClient(value = "mes-md-provider",contextId = "router", fallback = RouterFitServiceImpl.class, configuration = FallBackConfig.class)
public interface RouterFitService {

    /**
     * 根据物料、物料组和产线带出工艺路线和物料清单
     * @param orderType
     * @param itemBo
     * @param productBo
     * @return
     */
    @PostMapping("/routerFit/getRouterAndBom")
//    @ApiOperation(value = "根据物料、物料组和产线带出工艺路线和物料清单")
    ResponseData<RouterFitDto> getRouterAndBom(@RequestParam(value = "orderType", required = false) String orderType,
                                               @RequestParam(value = "itemBo",required = false) String itemBo,
                                               @RequestParam(value = "productBo",required = false) String productBo);

}
