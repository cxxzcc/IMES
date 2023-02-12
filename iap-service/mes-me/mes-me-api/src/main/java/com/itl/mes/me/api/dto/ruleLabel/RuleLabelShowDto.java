package com.itl.mes.me.api.dto.ruleLabel;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.mes.me.api.entity.RuleLabelDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
@ApiModel("物料规则模板listDto")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RuleLabelShowDto {
    @ApiModelProperty("bo")
    private String bo;

    @ApiModelProperty("元素类型")
    private String elementType;

    @ApiModelProperty("编码")
    private String ruleLabel;
    @ApiModelProperty("名称")
    private String ruleLabelName;

    @ApiModelProperty("codeRuleBo")
    private String codeRuleBo;
    @ApiModelProperty("编码规则类型")
    private String codeRuleType;

    @ApiModelProperty("labelBo")
    private String labelBo;
    @ApiModelProperty("标签模板")
    private String label;
    @ApiModelProperty("标签类型")
    private String labelType;
    @ApiModelProperty("模板类型")
    private String templateType;
    @ApiModelProperty("lodop生成的代码段")
    private String lodopText;

    @ApiModelProperty("变量字段对应关系")
    private List<RuleLabelDetail> details;
}
