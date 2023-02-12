package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.InWarehouseService;
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
public class InWarehouseServiceImpl implements InWarehouseService {

    @Override
    public List<Map<String, Object>> getInvoiceInWarehouse(List<String> inNos) {
        log.error("sorry,getInvoiceInWarehouse  feign fallback:{}", inNos);
        return null;
    }
}
