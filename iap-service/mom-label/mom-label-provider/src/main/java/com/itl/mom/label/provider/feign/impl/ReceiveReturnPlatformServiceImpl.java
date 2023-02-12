package com.itl.mom.label.provider.feign.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.itl.mom.label.provider.feign.ReceiveReturnPlatformService;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2021/12/24
 * @since 1.8
 */
@Component
@Slf4j
public class ReceiveReturnPlatformServiceImpl implements ReceiveReturnPlatformService {
    @Override
    public List<Map<String, Object>> getInvoiceReceiveReturn(List<String> bos) {
        log.error("sorry,getInvoiceReceiveReturn  feign fallback:{}", bos);
        return null;
    }
}
