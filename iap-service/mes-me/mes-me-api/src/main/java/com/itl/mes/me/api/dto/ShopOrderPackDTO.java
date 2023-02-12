package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "工单包装查询条件", description = "工单包装查询条件")
public class ShopOrderPackDTO {

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "条码")
    private String sn;


}