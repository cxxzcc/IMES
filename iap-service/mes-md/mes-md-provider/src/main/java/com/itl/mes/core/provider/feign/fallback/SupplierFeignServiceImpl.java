package com.itl.mes.core.provider.feign.fallback;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mes.core.provider.feign.SupplierFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 供应商调用fallback
 * @author dengou
 * @date 2021/9/29
 */
@Component
@Slf4j
public class SupplierFeignServiceImpl implements SupplierFeignService {
    @Override
    public ResponseData<Map<String, Object>> getById(String returnBos) {
        log.error("sorry SupplierFeignService getById feign fallback returnBos: {}", returnBos);
        return ResponseData.error("getById调用失败");
    }

    @Override
    public ResponseData<List<Map<String, Object>>> getByIds(List<String> ids) {
        log.error("sorry SupplierFeignService getById feign fallback returnBos: {}", ids);
        return ResponseData.error("getByIds调用失败");
    }

    @Override
    public ResponseData<Map<String, Object>> getByCode(String code) {
        log.error("sorry SupplierFeignService getByCode feign fallback returnBos: {}, {}", code);
        return ResponseData.error("getByCode调用失败");
    }
    @Override
    public List<String> queryItemBoByPurchaseOrderBO(String purchaseOrderBo, String site) {
        return null;
    }
}
