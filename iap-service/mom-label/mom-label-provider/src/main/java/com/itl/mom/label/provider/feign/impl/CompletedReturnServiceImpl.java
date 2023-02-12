package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.CompletedReturnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2021/5/19
 * @since 1.8
 */
@Component
@Slf4j
public class CompletedReturnServiceImpl implements CompletedReturnService {
    @Override
    public List<Map<String, Object>> getInvoiceReturn(List<String> returnBos) {
        log.error("sorry CompletedReturnService getInvoiceReturn feign fallback returnBos: {}", returnBos);
        return null;
    }
}
