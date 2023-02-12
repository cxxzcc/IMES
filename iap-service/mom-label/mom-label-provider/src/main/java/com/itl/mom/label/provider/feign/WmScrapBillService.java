package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.WmScrapBillServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cch
 * @date 2021/5/19$
 * @since JDK1.8
 */
@FeignClient(value = "wms-wm-provider",
        contextId = "WmScrapBillService",
        fallback = WmScrapBillServiceImpl.class)
public interface WmScrapBillService {

    /**
     * 获取搬运单数据
     * @param numList
     * @return
     */
    @ApiOperation(value = "获取搬运单数据")
    @PostMapping("porvider/wmScrapbill/getInvoiceScrapBill")
    List<Map<String,Object>> getInvoiceScrapBill(@RequestBody List<String> numList);

}
