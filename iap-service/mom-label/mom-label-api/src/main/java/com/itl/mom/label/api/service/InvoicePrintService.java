package com.itl.mom.label.api.service;

import com.itl.iap.common.base.exception.CommonException;
import com.itl.mom.label.api.dto.invoice.InvoicePrintDto;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2021/5/13 9:21
 */
public interface InvoicePrintService {

    /**
     * 单据打印
     * @return
     */
    List<String> invoicePrint(InvoicePrintDto invoicePrintDto) throws CommonException;

}
