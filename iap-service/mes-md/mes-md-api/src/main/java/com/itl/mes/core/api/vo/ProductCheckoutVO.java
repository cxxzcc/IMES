package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "产品检验数据集")
public class ProductCheckoutVO {

    @ApiModelProperty(value = "工单")
    private String workOrderNumber;

    @ApiModelProperty(value = "产线")
    private String productionLine;

    @ApiModelProperty(value = "项目id")
    private String projectId;

    @ApiModelProperty(value = "项目名称")
    private String projectName;

    @ApiModelProperty(value = "成功数")
    private Integer ok;

    @ApiModelProperty(value = "失败数")
    private Integer ng;

    @ApiModelProperty(value = "工厂")
    private String site;

}
