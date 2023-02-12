package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.AsnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author space
 * @date 2021/4/13
 * @since JDK1.8
 */
@Component
@Slf4j
public class AsnServiceImpl implements AsnService {


    @Override
    public List<Map<String,Object>> getInvoiceAsn(List<String> asnNos) {
        log.error("sorry,getInvoiceAsn  feign fallback:{}", asnNos);
        return null;
    }
}
