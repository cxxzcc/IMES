package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.SalesReturnServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author xtz
 * @date 2021/5/20
 * @since 1.8
 */
@FeignClient(value = "wms-iow-provider",contextId = "salesReturn", fallback = SalesReturnServiceImpl.class)
public interface SalesReturnService {
    @PostMapping("/salesReturn/getInvoiceSalesReturn")
    List<Map<String, Object>> getInvoiceSalesReturn(@RequestBody List<String> returnBos);
}
