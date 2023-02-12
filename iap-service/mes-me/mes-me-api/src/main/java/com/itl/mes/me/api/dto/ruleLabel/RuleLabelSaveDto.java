package com.itl.mes.me.api.dto.ruleLabel;

import com.itl.mes.me.api.entity.RuleLabelDetail;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
public class RuleLabelSaveDto {
    @ApiModelProperty("bo")
    private String bo;
    @ApiModelProperty(value = "编号")
    private String ruleLabel;
    @ApiModelProperty(value = "名字")
    private String ruleLabelName;
    @ApiModelProperty("元素类型(物料/工单/设备/容器/载具)")
    private String elementType;
    @ApiModelProperty("编码规则")
    private String codeRuleType;
    @ApiModelProperty("标签")
    private String labelBo;
    @ApiModelProperty("标签类型")
    private String labelType;
    @ApiModelProperty("工厂")
    private String site;
    @ApiModelProperty("变量字段对应关系")
    private List<RuleLabelDetail> details;
}
