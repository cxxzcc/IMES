package com.itl.mom.label.provider.feign;

/**
 * @author FENGRR
 * @date 2021/5/21
 * @since JDK1.8
 */


import com.itl.mom.label.provider.feign.impl.PurchaseOrderServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author FengRR
 * @date 2021/5/20
 * @since JDK1.
 */
@FeignClient(value = "wms-portal-provider",
        contextId = "purchaseOrderNum",
        fallback = PurchaseOrderServiceImpl.class)
public interface PurchaseOrderService {

    /**
     * getFields
     * 获取采购订单数据
     * @param returnBos
     * @return
     */
    @PostMapping("portal/purchaseOrder/getInvoicePurchaseOrder")
    @ApiOperation(value = "获得采购订单信息", notes = "获得采购订单信息")
    List<Map<String, Object>> getInvoicePurchaseOrder(@RequestBody List<String> returnBos);
}
