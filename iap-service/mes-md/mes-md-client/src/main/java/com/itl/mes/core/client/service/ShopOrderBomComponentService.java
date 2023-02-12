package com.itl.mes.core.client.service;


import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.ShopOrderBomComponentServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author space
 * @date 2021/4/13
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "ShopOrderBomComponent",
        fallback = ShopOrderBomComponentServiceImpl.class,
        configuration = FallBackConfig.class)
public interface ShopOrderBomComponentService {

    /**
     * 根据工单Bo查询工单工序Bom
     * @param shopOrderBo
     * @return
     */
    @GetMapping("/shopOrderBomComponnet/queryBomByShopOrderBo")
    @ApiOperation(value="querynBomByShopOrderBo for feign")
    List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(@RequestParam("shopOrderBo") String shopOrderBo,
                                                        @RequestParam("type") String type);
}
