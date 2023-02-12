package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.SalesReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author xtz
 * @date 2021/5/20
 * @since 1.8
 */
@Component
@Slf4j
public class SalesReturnServiceImpl implements SalesReturnService {
    @Override
    public List<Map<String, Object>> getInvoiceSalesReturn(List<String> returnBos) {
        log.error("sorry SalesReturnService getInvoiceSalesReturn feign fallback returnBos: {}", returnBos);
        return null;
    }
}
