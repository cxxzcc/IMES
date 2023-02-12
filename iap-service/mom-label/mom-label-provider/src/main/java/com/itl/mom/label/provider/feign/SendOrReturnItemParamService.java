package com.itl.mom.label.provider.feign;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.provider.feign.impl.SendOrReturnItemParamServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * description:
 * author: lK
 * time: 2021/5/26 11:02
 */
@FeignClient(value = "wms-iow-provider",fallback = SendOrReturnItemParamServiceImpl.class)
public interface SendOrReturnItemParamService {

    //发料(领料)打印所需
    @PostMapping("/iowSendItem/getInvoiceSendItem")
    List<Map<String, Object>> getInvoiceSendItem(@RequestBody List<String> sendItemBos);

    //退料 打印所需
    @PostMapping("/iowReturnItem/getInvoiceReturnItem")
    List<Map<String, Object>> getInvoiceReturnItem(@RequestBody List<String> boS);

    //销售出库 打印所需
    @PostMapping("/salesOut/printParam")
    List<Map<String, Object>> getInvoiceSalesOut(@RequestBody List<String> boS) throws CommonException;
}
