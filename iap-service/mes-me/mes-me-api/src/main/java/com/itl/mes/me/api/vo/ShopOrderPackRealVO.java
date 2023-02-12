package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 载具表
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@Data
@ApiModel(value = "工单包装数据列表", description = "工单包装数据列表")
public class ShopOrderPackRealVO {

    @ApiModelProperty(value = "工单bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "父包装号")
    private String packParentNo;

    @ApiModelProperty(value = "包装号")
    private String packNo;

    @ApiModelProperty(value = "包装层级")
    private String packLevel;

    @ApiModelProperty(value = "包装名称")
    private String packName;

    @ApiModelProperty(value = "最大包装数")
    private String max;


}