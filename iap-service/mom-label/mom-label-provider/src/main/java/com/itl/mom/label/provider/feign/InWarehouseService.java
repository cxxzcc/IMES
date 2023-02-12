package com.itl.mom.label.provider.feign;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.provider.feign.impl.InWarehouseServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

/**
 * @author liuchenghao
 * @date 2021/5/19 17:59
 */
@FeignClient(value = "wms-portal-provider",
        contextId = "inWarehouseService",
        fallback = InWarehouseServiceImpl.class)
public interface InWarehouseService {


    /**
     * 获取来料入库单单据信息
     * @param inNos
     * @return
     * @throws CommonException
     */
    @PostMapping("/inWarehouse/getInvoiceInWarehouse")
    @ApiOperation(value = "获取asn单据信息")
    List<Map<String,Object>> getInvoiceInWarehouse(List<String> inNos) throws CommonException;
}
