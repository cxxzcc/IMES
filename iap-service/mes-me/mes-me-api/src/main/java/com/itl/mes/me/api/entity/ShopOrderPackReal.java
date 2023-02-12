package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("p_shop_order_pack_real")
@ApiModel(value = "工单包装存储", description = "工单包装存储")
public class ShopOrderPackReal {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "工位bo")
    @TableField(value = "station_bo")
    private String stationBo;

    @ApiModelProperty(value = "工单bo")
    @TableField(value = "shop_order_bo")
    private String shopOrderBo;

    @ApiModelProperty(value = "包装规则bo")
    @TableField(value = "pack_bo")
    private String packBo;

    @ApiModelProperty(value = "包装号")
    @TableField(value = "pack_no")
    private String packNo;

    @ApiModelProperty(value = "层级")
    @TableField(value = "pack_level")
    private String packLevel;

    @ApiModelProperty(value = "父包装号")
    @TableField(value = "pack_parent_no")
    private String packParentNo;

    @ApiModelProperty(value = "最大个数")
    @TableField(value = "max")
    private String max;

    @ApiModelProperty(value = "site")
    @TableField(value = "site")
    private String site;


}