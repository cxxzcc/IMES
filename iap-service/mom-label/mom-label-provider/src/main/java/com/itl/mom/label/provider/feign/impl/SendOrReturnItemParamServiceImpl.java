package com.itl.mom.label.provider.feign.impl;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.provider.feign.SendOrReturnItemParamService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * description:
 * author: lK
 * time: 2021/5/26 11:04
 */
@Component
@Slf4j
public class SendOrReturnItemParamServiceImpl implements SendOrReturnItemParamService {

    //发料
    @Override
    public List<Map<String, Object>> getInvoiceSendItem(List<String> sendItemBos) {
        log.error("sorry SendOrReturnItemParamService getInvoiceSendItem feign fallback sendItemBos: {}", sendItemBos);
        return null;
    }

    //退料
    @Override
    public List<Map<String, Object>> getInvoiceReturnItem(List<String> boS) {
        log.error("sorry SendOrReturnItemParamService getInvoiceReturnItem feign fallback boS: {}", boS);
        return null;
    }

    //销售出库
    @Override
    public List<Map<String, Object>> getInvoiceSalesOut(List<String> boS) throws CommonException {
        log.error("sorry SendOrReturnItemParamService getInvoiceSalesOut feign fallback boS: {}", boS);
        return null;
    }


}
