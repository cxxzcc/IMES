package com.itl.mes.core.client.service.impl;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.api.dto.MboMitemDTO;
import com.itl.mes.core.api.vo.ShopOrderBomComponnetVo;
import com.itl.mes.core.api.vo.ShopOrderFullVo;
import com.itl.mes.core.client.service.ShopOrderBomComponentService;
import com.itl.mes.core.client.service.ShopOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author space
 * @date 2021/4/13
 * @since JDK1.8
 */
@Component
@Slf4j
public class ShopOrderBomComponentServiceImpl implements ShopOrderBomComponentService {
    @Override
    public List<ShopOrderBomComponnetVo> queryBomByShopOrderBo(String shopOrderBo, String type) {
        log.error("sorry ShopOrderBomComponentService queryBomByShopOrderBo feign fallback, shopOrderBo:{}, type:{}", shopOrderBo, type);
        return null;
    }
}
