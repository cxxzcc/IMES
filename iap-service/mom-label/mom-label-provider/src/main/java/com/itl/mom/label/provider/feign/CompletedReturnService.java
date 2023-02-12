package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.CompletedReturnServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2021/5/19
 * @since 1.8
 */
@FeignClient(value = "wms-iow-provider",contextId = "completedReturn", fallback = CompletedReturnServiceImpl.class)
public interface CompletedReturnService {
    @PostMapping("/completedReturn/getInvoiceReturn")
    List<Map<String, Object>> getInvoiceReturn(@RequestBody List<String> returnBos);
}
