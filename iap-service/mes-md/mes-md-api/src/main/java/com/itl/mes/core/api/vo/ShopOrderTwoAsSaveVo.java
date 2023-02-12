package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import java.io.Serializable;


@Data
@ApiModel(value = "ShopOrderTwoAsSaveVo", description = "工单Two维护另存VO")
public class ShopOrderTwoAsSaveVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "工单bo")
    private String bo;

    @ApiModelProperty(value = "工单号")
    @NotBlank
    private String shopOrder;

    @ApiModelProperty(value = "工单描述")
    private String orderDesc;

    @ApiModelProperty(value = "工单类型")
    private String shopOrderType;

    @ApiModelProperty(value ="产线BO")
    private String productionLineBo;

    @ApiModelProperty(value ="产线编号")
    private String productionLineCode;

    @ApiModelProperty(value ="产线描述")
    private String productionLineDesc;

    @ApiModelProperty(value ="班次BO")
    private String shiftBo;

    @ApiModelProperty(value ="班次编号")
    private String shiftCode;

}
