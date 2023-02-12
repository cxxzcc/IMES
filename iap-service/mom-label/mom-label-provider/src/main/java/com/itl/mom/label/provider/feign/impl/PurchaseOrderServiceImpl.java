package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.PurchaseOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author FengRR
 * @date 2021/5/21
 * @since JDK1.8
 */
@Component
@Slf4j
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    @Override
    public List<Map<String, Object>> getInvoicePurchaseOrder(List<String> returnBos) {
        log.error("sorry OrderNumService getInvoicePurchaseOrder feign fallback returnBos: {}", returnBos);
        return null;
    }
}

