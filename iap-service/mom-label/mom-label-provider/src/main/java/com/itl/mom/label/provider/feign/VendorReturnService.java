package com.itl.mom.label.provider.feign;


import com.itl.mom.label.provider.feign.impl.VendorReturnServiceImpl;
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
@FeignClient(value = "wms-portal-provider",contextId = "vendorReturn", fallback = VendorReturnServiceImpl.class)
public interface VendorReturnService {
    @PostMapping("/vendorReturn/getInvoiceVendorReturn")
    List<Map<String, Object>> getInvoiceVendorReturn(@RequestBody List<String> returnBos);
}
