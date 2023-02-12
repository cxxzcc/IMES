package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.WmTransferBillServiceImpl;
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
        contextId = "wmTransferBillService",
        fallback = WmTransferBillServiceImpl.class)
public interface WmTransferBillService {

    /**
     * 获取单据转移数据
     * @param numList
     * @return
     */
    @ApiOperation(value = "获取单据转移数据")
    @PostMapping("/transfer/bill/batch/transferBill")
    List<Map<String,Object>> getInvoiceTransferBill(@RequestBody List<String> numList);
}
