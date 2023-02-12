package com.itl.mes.core.provider.feign;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.provider.feign.fallback.SupplierFeignServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 供应商服务
 * @author dengou
 * @date 2021/9/29
 */
@FeignClient(value = "wms-portal-provider", fallback = SupplierFeignServiceImpl.class)
public interface SupplierFeignService {

    @GetMapping("/supplier/getById/{id}")
    ResponseData<Map<String, Object>> getById(@PathVariable String id);

    @PostMapping("/supplier/getByIds")
    ResponseData<List<Map<String, Object>>> getByIds(@RequestBody List<String> ids);

    /**
     * bo: bo
     * vendName : 供应商名称
     * */
    @GetMapping("/supplier/getByCode/{code}")
    ResponseData<Map<String, Object>> getByCode(@PathVariable("code") String code);

    @GetMapping("/portal/purchaseOrder/queryItemBoByPurchaseOrderBO")
    List<String> queryItemBoByPurchaseOrderBO(@RequestParam("purchaseOrderBo") String purchaseOrderBo, @RequestParam("site") String site);

}
