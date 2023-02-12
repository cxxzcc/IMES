package com.itl.mes.core.provider.feign;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.provider.feign.fallback.CustomerFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务
 * @author dengou
 * @date 2021/9/29
 */
@FeignClient(value = "wms-portal-provider", fallback = CustomerFeignServiceImpl.class)
public interface CustomerFeignService {

    @PostMapping("/customer/getByIds")
    ResponseData<List<Map<String, Object>>> getByIds(@RequestBody List<String> ids);
}
