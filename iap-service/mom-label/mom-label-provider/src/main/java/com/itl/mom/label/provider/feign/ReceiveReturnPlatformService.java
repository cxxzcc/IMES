package com.itl.mom.label.provider.feign;

import com.itl.mom.label.provider.feign.impl.ReceiveReturnPlatformServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author yx
 * @date 2021/5/20
 * @since JDK1.8
 */
@FeignClient(value = "wms-wm-provider",
        contextId = "ReceiveReturnPlatformService",
        fallback = ReceiveReturnPlatformServiceImpl.class)
public interface ReceiveReturnPlatformService {

    @PostMapping("/receiveReturnPlatform/getInvoiceReceiveReturn")
    List<Map<String, Object>> getInvoiceReceiveReturn(@RequestBody List<String> bos);

}
