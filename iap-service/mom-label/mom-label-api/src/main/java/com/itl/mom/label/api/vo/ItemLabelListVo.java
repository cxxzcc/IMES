package com.itl.mom.label.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel(value = "ItemLabelListVo",description = "物料标签列表返回实体")
public class ItemLabelListVo {

    @ApiModelProperty(value = "主键ID")
    private String id;

    @ApiModelProperty(value = "物料标签编码")
    private String sn;

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "追溯方式")
    private String zsType;

    @ApiModelProperty(value = "批量数")
    private BigDecimal lotSize;

    @ApiModelProperty(value = "剩余数量")
    private BigDecimal sysl;

    @ApiModelProperty(value = "标签状态")
    private String state;

    @ApiModelProperty(value = "工单")
    private String shopOrderBo;

    @ApiModelProperty("批次")
    private String pc;

    @ApiModelProperty("是否挂起")
    private String sfgq;

    @ApiModelProperty("是否挂起名称")
    private String sfgqName;
}
