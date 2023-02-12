package com.itl.mom.label.provider.feign.impl;

import com.itl.mom.label.provider.feign.WmInventoryBillService;
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
public class WmInventoryBillServiceImpl implements WmInventoryBillService {

    /**
     * 获取单据盘点数据
     *
     * @param inventoryBillBoList
     * @return
     */
    @Override
    public List<Map<String, Object>> getInvoiceInventoryBill(List<String> inventoryBillBoList) {
        log.error("sorry WmInventoryBillService getInvoiceInventoryBill  feign fallback inventoryBillBoList:{}", inventoryBillBoList);
        return null;
    }
}
