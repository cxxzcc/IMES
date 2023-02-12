package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2021/4/16 9:49
 */
@Data
@ApiModel(value = "ItemAndCustomDataValVo",description = "物料和自定义字段值的返回实体")
public class ItemAndCustomDataValVo {

    @ApiModelProperty(value = "物料主键")
    private String itemBo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;


    @ApiModelProperty(value = "物料单位")
    private String itemUnit;

    @ApiModelProperty(value = "金额")
    private String money;


    @ApiModelProperty(value = "物料净重")
    private String weight;

    @ApiModelProperty(value = "体积")
    private String volume;

    @ApiModelProperty(value = "规格型号")
    private String specificationModel;

    @ApiModelProperty(value = "简称")
    private String abbreviation;

}
