package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.ShipmentOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2021/5/19 18:01
 */
@Component
@Slf4j
public class ShipmentOrderServiceImpl implements ShipmentOrderService {

    @Override
    public List<Map<String, Object>> getInvoiceShipmentOrder(List<String> oddNos)  {
        log.error("sorry,getInvoiceShipmentOrder  feign fallback:{}", oddNos);
        return null;
    }
}
