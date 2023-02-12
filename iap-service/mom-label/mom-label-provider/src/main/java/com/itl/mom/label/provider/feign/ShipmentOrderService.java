package com.itl.mom.label.provider.feign;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.provider.feign.impl.ShipmentOrderServiceImpl;
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
        contextId = "shipmentOrderService",
        fallback = ShipmentOrderServiceImpl.class)
public interface ShipmentOrderService {

    /**
     * 获取发运单单据信息
     * @param oddNos
     * @return
     * @throws CommonException
     */
    @PostMapping("/shipmentOrder/getInvoiceShipmentOrder")
    @ApiOperation(value = "获取发运单单据信息")
    List<Map<String,Object>> getInvoiceShipmentOrder(List<String> oddNos) throws CommonException;
}
