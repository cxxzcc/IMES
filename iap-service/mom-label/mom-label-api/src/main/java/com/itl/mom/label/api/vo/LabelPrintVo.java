package com.itl.mom.label.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/1/26
 */
@Data
@ApiModel(value = "LabelPrintVo",description = "标签打印返回实体")
public class LabelPrintVo {

    @ApiModelProperty(value = "主键ID")
    private String bo;


    @ApiModelProperty(value = "打印日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date printDate;

    @ApiModelProperty(value = "根据不同的类型显示不同的code")
    private String elementCode;

    @ApiModelProperty(value = "条码数量")
    private Integer barCodeAmount;

    @ApiModelProperty(value = "开始编码")
    private String startCode;

    @ApiModelProperty(value = "结束编码")
    private String endCode;

    @ApiModelProperty(value = "标签模板类型")
    private String templateType;

    @ApiModelProperty(value = "lodop代码段")
    private String lodopText;

    @ApiModelProperty(value = "是否补码")
    private Integer isComplement;

    @ApiModelProperty("编码规则类型")
    private String codeRuleType;

    @ApiModelProperty("标签模板")
    private String label;
    @ApiModelProperty("标签类型")
    private String labelType;
    @ApiModelProperty("规则模板BO")
    private String ruleLabelBo;


    @ApiModelProperty("规则模板编码")
    private String ruleLabel;

    @ApiModelProperty("规则模板名称")
    private String ruleLabelName;


    @ApiModelProperty("包装名称，包装页面显示")
    private String pickName;

    @ApiModelProperty("物料编码，包装页面显示")
    private String item;

    @ApiModelProperty("工单，包装页面显示")
    private String shopOrder;

    @ApiModelProperty(value = "生成日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createDate;
}
