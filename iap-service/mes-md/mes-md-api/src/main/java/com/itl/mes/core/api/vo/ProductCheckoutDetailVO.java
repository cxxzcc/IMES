package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "产品检验详情数据集")
public class ProductCheckoutDetailVO {

    @ApiModelProperty(value = "条码")
    private String barCode;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNumber;

    @ApiModelProperty(value = "是否hold")
    private String hold;

    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

    @ApiModelProperty(value = "工单数量")
    private String workCount;

    @ApiModelProperty(value = "工艺流程名称")
    private String processName;

    @ApiModelProperty(value = "车间")
    private String workshop;

    @ApiModelProperty(value = "产线")
    private String productionLine;

    @ApiModelProperty(value = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "检验项目名称")
    private String projectName;

    @ApiModelProperty(value = "规范上限")
    private String uppperLimit;

    @ApiModelProperty(value = "规范下限")
    private String lowerLimit;

    @ApiModelProperty(value = "测试值")
    private String test;

    @ApiModelProperty(value = "检验结果")
    private String result;

    @ApiModelProperty(value = "状态")
    private String state;

    @ApiModelProperty(value = "检验人")
    private String surveyor;

    @ApiModelProperty(value = "操作时间")
    private String createTime;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "工序")
    private String process;

    @ApiModelProperty(value = "班次")
    private String classes;




}
