package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.WmScrapBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author cch
 * @date 2021/5/19$
 * @since JDK1.8
 */
@Component
@Slf4j
public class WmScrapBillServiceImpl implements WmScrapBillService {
    @Override
    public List<Map<String, Object>> getInvoiceScrapBill(List<String> numList) {
        log.error("sorry WmScrapBillServiceImpl getInvoiceScrapBill  feign fallback numList:{}", numList);
        return null;
    }
}
