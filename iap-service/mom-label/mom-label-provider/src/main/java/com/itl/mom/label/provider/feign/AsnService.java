package com.itl.mom.label.provider.feign;


import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.provider.feign.impl.AsnServiceImpl;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author cch
 * @date 2021/4/13
 * @since JDK1.8
 */
@FeignClient(value = "wms-portal-provider",
        contextId = "asnService",
        fallback = AsnServiceImpl.class)
public interface AsnService {


    /**
     * 获取单据Asn信息
     * @param asnNos
     * @return
     * @throws CommonException
     */
    @PostMapping("/asn/getInvoiceAsn")
    @ApiOperation(value = "获取asn单据信息")
    List<Map<String,Object>> getInvoiceAsn(@RequestBody List<String> asnNos) throws CommonException;
}
