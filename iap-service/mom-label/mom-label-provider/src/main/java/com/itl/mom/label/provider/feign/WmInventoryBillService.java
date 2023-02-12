package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.WmInventoryBillServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author zhongfei
 * @date 2021/5/19
 * @since JDK1.8
 */
@FeignClient(value = "wms-wm-provider",
        contextId = "wmInventoryBillService",
        fallback = WmInventoryBillServiceImpl.class)
public interface WmInventoryBillService {

    /**
     * 获取单据盘点数据
     * @param inventoryBillBoList
     * @return
     */
    @ApiOperation(value = "获取单据盘点数据")
    @PostMapping("/inventory/bill/batch/inventoryBill")
    List<Map<String,Object>> getInvoiceInventoryBill(@RequestBody List<String> inventoryBillBoList);
}
