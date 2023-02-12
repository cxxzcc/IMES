package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @auth liuchenghao
 * @date 2021/5/27
 */
@Data
@ApiModel(value = "ShopOrderBomComponnetVo",description = "工单bom组件查询返回实体")
public class ShopOrderBomComponnetVo {


    @ApiModelProperty(value="BC:SITE,BOM,COMPONENT【PK】")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="编号【UK】")
    private Integer sequence;

    @ApiModelProperty(value="组件【M_ITEM的item】")
    private String component;

    @ApiModelProperty(value="组件Bo【M_ITEM的itemBo】")
    private String componentBo;

    @ApiModelProperty("组件的描述")
    private String itemDesc;


    @ApiModelProperty(value="装配工序【M_OPERATION的operation】")
    private String operation;

    @ApiModelProperty(value="装配工序Bo【M_OPERATION的operationBo】")
    private String operationBo;


    @ApiModelProperty(value="组件类型")
    private String itemType;
    @ApiModelProperty(value="组件装配顺序")
    private String itemOrder;
    @ApiModelProperty(value="虚拟件支持")
    private String virtualItem;


    @ApiModelProperty(value="追溯方式")
    private String zsType;

    @ApiModelProperty(value="装配类型")
    private String assType;

    @ApiModelProperty(value="组件位置")
    private String componentPosition;


    @ApiModelProperty(value="装配数量")
    private BigDecimal qty;


    @ApiModelProperty(value = "组件M_ITEM的Bo")
    private String itemBo;
    @ApiModelProperty(value = "单体条码绑定的Sn")
    private String itemSn;
    @ApiModelProperty(value = "是否装配完成")
    private Boolean assyFinish;
}
