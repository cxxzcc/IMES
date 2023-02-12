package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物料BOM相关参数
 *
 * @author GKL
 * @date 2021/11/5 - 9:15
 * @since 2021/11/5 - 9:15 星期五 by GKL
 */
@Data
@ApiModel(value = "CheckBatchSnItemCodeAndSnDto", description = "物料BOM相关参数")
public class CheckBatchSnItemCodeAndSnDto {

    @ApiModelProperty(value = "组件装配顺序")
    private String itemOrder;


    @ApiModelProperty(value = "物料BOM(组件)")
    private String itemBom;

    @ApiModelProperty(value = "物料bo")
    private String itemBo;

    @ApiModelProperty(value = "装配工序")
    private String operation;


    @ApiModelProperty(value = "BOM数量(装配数量)")
    private BigDecimal bomCount;


    @ApiModelProperty(value = "装配位置")
    private String componentPosition;

    @ApiModelProperty(value = "追溯方式")
    private String zsType;

    @ApiModelProperty(value = "组件类型")
    private String itemType;

    @ApiModelProperty(value = "已上料数量")
    private BigDecimal loadedCount;


    @ApiModelProperty(value = "虚拟件支持")
    private String virtualItem;

    @ApiModelProperty(value = "bom组件Bo")
    private String shopOrderBomComponentBo;

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;
}
