package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * @auth liuchenghao
 * @date 2021/5/26
 *
 * 工单BOM表
 */
@Data
@TableName("m_shop_order_bom_componnet")
@ApiModel(value = "工单BOM组件表")
public class ShopOrderBomComponnet {

    @ApiModelProperty(value="主键")
    @Length( max = 100 )
    @TableId(value = "BO", type = IdType.UUID)
    private String bo;

    @ApiModelProperty(value="工厂")
    @Length( max = 32 )
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="工单BO")
    @Length( max = 100 )
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

    @ApiModelProperty(value="编号【UK】")
    @TableField("SEQUENCE")
    private Integer sequence;

    @ApiModelProperty(value="组件【M_ITEM的BO】")
    @Length( max = 100 )
    @TableField("COMPONENT_BO")
    private String componentBo;

    @ApiModelProperty(value="物料单位")
    @Length( max = 100 )
    @TableField("ITEM_UNIT")
    private String itemUnit;

    @ApiModelProperty(value="装配工序【M_OPERATION的BO】")
    @Length( max = 100 )
    @TableField("OPERATION_BO")
    private String operationBo;

    @ApiModelProperty(value="是否使用替代组件")
    @Length( max = 1 )
    @TableField("IS_USE_ALTERNATE_COMPONENT")
    private String isUseAlternateComponent;

    @ApiModelProperty(value="追溯方式")
    @Length( max = 1 )
    @TableField("ZS_TYPE")
    private String zsType;

    @ApiModelProperty(value="装配类型")
    @Length( max = 32 )
    @TableField("ASS_TYPE")
    private String assType;

    @ApiModelProperty(value="组件位置")
    @Length( max = 64 )
    @TableField("COMPONENT_POSITION")
    private String componentPosition;

    @ApiModelProperty(value="参考指示符")
    @Length( max = 30 )
    @TableField("REFERENCE")
    private String reference;

    @ApiModelProperty(value="装配数量")
    @TableField("QTY")
    @Min(value = 0,message="装配数量不能为负值")
    private BigDecimal qty;


    @ApiModelProperty(value="组件类型")
    @TableField("ITEM_TYPE")
    private String itemType;

    @ApiModelProperty(value="组件装配顺序")
    @TableField("ITEM_ORDER")
    private String itemOrder;

    @ApiModelProperty(value="虚拟件支持")
    @TableField("VIRTUAL_ITEM")
    private String virtualItem;

    @TableField("TYPE")
    @ApiModelProperty(value = "类型，SO代表工单，OP代表工序")
    private String type;

    @TableField("LOADED_COUNT")
    @ApiModelProperty(value = "已上料数量")
    private BigDecimal loadedCount;

}
