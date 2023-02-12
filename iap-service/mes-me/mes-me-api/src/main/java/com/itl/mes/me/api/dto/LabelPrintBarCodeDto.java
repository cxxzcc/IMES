package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author zhongfei
 * @date 2021/4/19 15:16
 */
@Data
@ApiModel(value = "LabelPrintBarCodeDto",description = "标签打印请求实体")
public class LabelPrintBarCodeDto {

    @ApiModelProperty(value = "打印BO(可以是打印范围BO或者打印明细BO)")
    private String bo;

    @ApiModelProperty(value = "并行数量")
    private Integer parallelAmount;

    @ApiModelProperty(value = "并行参数")
    private List<String> labelParamsList;

    @ApiModelProperty(value = "并行单个打印开始条码")
    private String startBarCode;

    @ApiModelProperty(value = "并行单个打印截止条码")
    private String endBarCode;


}
