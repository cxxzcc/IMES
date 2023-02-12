package com.itl.mom.label.api.dto.invoice;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liuchenghao
 * @date 2021/5/13 9:23
 */
@Data
@ApiModel(value = "InvoicePrintDto",description = "单据打印请求实体")
public class InvoicePrintDto {

    /**
     * 单据单号集合
     */
    @ApiModelProperty(value = "单据单号集合")
    private List<String> invoiceNos;

    @ApiModelProperty(value = "单据类型")
    private String invoiceType;


}
