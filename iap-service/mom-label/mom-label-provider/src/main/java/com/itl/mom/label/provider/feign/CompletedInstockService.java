package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.CompletedInstockServiceImpl;
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
@FeignClient(value = "wms-iow-provider",contextId = "completedStock", fallback = CompletedInstockServiceImpl.class)
public interface CompletedInstockService {
    @PostMapping("/completedInstock/getInvoiceInstock")
    List<Map<String, Object>> getInvoiceInstock(@RequestBody List<String> instockBos);
}
