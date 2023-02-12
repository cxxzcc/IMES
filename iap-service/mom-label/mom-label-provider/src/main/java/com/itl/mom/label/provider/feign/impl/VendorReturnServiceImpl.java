package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.VendorReturnService;
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
public class VendorReturnServiceImpl implements VendorReturnService {
    @Override
    public List<Map<String, Object>> getInvoiceVendorReturn(List<String> returnBos) {
        log.error("sorry VendorReturnService getInvoiceVendorReturn feign fallback returnBos: {}", returnBos);
        return null;
    }
}
