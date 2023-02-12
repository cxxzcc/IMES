package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.entity.OrderRouter;
import com.itl.mes.core.client.service.OrderRouterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author cch
 * @date 2021/10/27$
 * @since JDK1.8
 */
@Slf4j
@Component
public class OrderRouterServiceImpl implements OrderRouterService {

    @Override
    public ResponseData<OrderRouter> getOrderRouter(String shopOrderBo) {
        log.error("sorry RouterService getRouter feign fallback, shopOrderBo:{}" + shopOrderBo);
        return null;
    }
}
