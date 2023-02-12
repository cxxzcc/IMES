package com.itl.mom.label.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/1/26
 */
@Data
@ApiModel(value = "SnVo",description = "标签打印详细信息返回实体")
public class SnVo {

    @ApiModelProperty(value = "标签打印详细BO，主键")
    private String bo;

    @ApiModelProperty(value = "根据不同的类型显示不同的code")
    private String elementCode;


    @ApiModelProperty(value = "详细条码")
    private String detailCode;

    @ApiModelProperty(value = "最后的操作人员")
    private String lastPrintUser;

    @ApiModelProperty(value = "最后的打印日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date lastPrintDate;

    @ApiModelProperty(value = "打印次数")
    private Integer printCount;


    @ApiModelProperty(value = "包装数量")
    private BigDecimal packingQuantity;


    @ApiModelProperty(value = "包装最大数量")
    private BigDecimal packingMaxQuantity;


    @ApiModelProperty(value = "状态")
    private String state;

}
