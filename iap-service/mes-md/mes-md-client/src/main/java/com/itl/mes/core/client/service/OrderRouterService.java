package com.itl.mes.core.client.service;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.client.config.FallBackConfig;
import com.itl.mes.core.client.service.impl.OrderRouterServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author cch
 * @date 2021/10/27$
 * @since JDK1.8
 */
@FeignClient(value = "mes-md-provider",contextId = "OrderRouterService", fallback = OrderRouterServiceImpl.class, configuration = FallBackConfig.class)
public interface OrderRouterService {

    /**
     * 根据工单bo查询工单工艺路线副本
     * @param shopOrderBo 工单bo
     * */
    @GetMapping("/orderRouter/query")
    ResponseData<OrderRouter> getOrderRouter(@RequestParam("shopOrderBo") String shopOrderBo);
}
