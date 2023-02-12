package com.itl.mom.label.provider.controller;

import com.itl.iap.common.base.response.ResponseData;
import com.itl.mom.label.api.dto.invoice.InvoicePrintDto;
import com.itl.mom.label.api.service.InvoicePrintService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuchenghao
 * @date 2021/5/18 10:04
 */
@Api(tags = "单据打印")
@RestController
@RequestMapping("/invoicePrint")
public class InvoicePrintController {

    @Autowired
    InvoicePrintService invoicePrintService;

    @PostMapping("/invoicePrint")
    @ApiOperation(value = "单据打印")
    public ResponseData invoicePrint(@RequestBody InvoicePrintDto invoicePrintDto) {
        return ResponseData.success(invoicePrintService.invoicePrint(invoicePrintDto));
    }



}
