package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.CompletedInstockService;
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
public class CompletedInstockServiceImpl implements CompletedInstockService {
    @Override
    public List<Map<String, Object>> getInvoiceInstock(List<String> instockBos) {
        log.error("sorry CompletedInstockService getInvoiceInstock feign fallback instockBos: {}", instockBos);
        return null;
    }
}
