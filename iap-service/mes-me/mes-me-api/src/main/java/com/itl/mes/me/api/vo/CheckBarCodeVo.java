package com.itl.mes.me.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @auth liuchenghao
 * @date 2021/1/26
 */
@Data
@ApiModel(value = "CheckBarCodeVo",description = "检查条码返回的实体")
public class CheckBarCodeVo implements Serializable {

    @ApiModelProperty(value = "标签打印详细BO，主键")
    private String bo;

    @ApiModelProperty(value = "包含的物料数量")
    private Integer amount;


    @ApiModelProperty(value = "物料BO")
    private String itemBo;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "物料描述")
    private String itemDesc;

    @ApiModelProperty(value = "可使用的物料数量")
    private Integer useAmount;

}
