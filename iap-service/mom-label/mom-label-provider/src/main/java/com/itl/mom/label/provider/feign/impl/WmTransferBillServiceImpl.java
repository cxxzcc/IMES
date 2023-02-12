package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.WmTransferBillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zhongfei
 * @date 2021/5/19
 * @since JDK1.8
 */
@Component
@Slf4j
public class WmTransferBillServiceImpl implements WmTransferBillService {

    /**
     * 获取单据转移数据
     *
     * @param numList
     * @return
     */
    @Override
    public List<Map<String, Object>> getInvoiceTransferBill(List<String> numList) {
        log.error("sorry WmTransferBillService getInvoiceTransferBill  feign fallback numList:{}", numList);
        return null;
    }
}
