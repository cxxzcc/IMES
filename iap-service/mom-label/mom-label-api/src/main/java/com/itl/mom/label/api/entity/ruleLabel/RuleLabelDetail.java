package com.itl.mom.label.api.entity.ruleLabel;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 规则模板明细表
 *
 * @author yx
 * @date 2021-01-21 14:06:32
 */
@Data
@TableName("label_rule_label_detail")
@ApiModel(value = "label_rule_label_detail", description = "规则模板明细")
@Accessors(chain = true)
public class RuleLabelDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * item_rule_label主键
     */
    @TableField("IRL_BO")
    @ApiModelProperty(value = "item_rule_label主键")
    private String irlBo;
    /**
     * 变量名
     */
    @ApiModelProperty(value = "变量名")
    @TableField("RULE_VAR")
    private String ruleVar;
    /**
     * 对应字段
     */
    @ApiModelProperty(value = "主表字段")
    @TableField("RULE_VAL")
    private String ruleVal;

    /**
     * 是否是自定义字段
     */
    @ApiModelProperty(value = "是否是自定义字段")
    @TableField("IS_CUSTOM")
    private String isCustom;

    /**
     * 标签模板变量
     */
    @ApiModelProperty(value = "模板变量")
    @TableField("TEMPLATE_ARG")
    private  String templateArg;

    @ApiModelProperty("是否明细 for wms")
    @TableField("IS_DETAIL")
    private String isDetail;

    @ApiModelProperty("标签参数类型")
    @TableField("LABEL_PARAM_TYPE")
    private String labelParamType;

    /**
     * 类型
     */
    @ApiModelProperty(value = "类型")
    @TableField("TYPE")
    private  String type;

    @ApiModelProperty("标签变量描述 for wms")
    @TableField("NAME")
    private String name;
}
