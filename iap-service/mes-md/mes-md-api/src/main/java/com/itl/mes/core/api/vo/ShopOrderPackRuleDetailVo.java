package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 */
@Data
@ApiModel(value = "ShopOrderPackRuleDetailVo",description = "工单包装规则明细查询返回实体")
public class  ShopOrderPackRuleDetailVo {

    @ApiModelProperty(value = "BO")
    private String bo;

    @ApiModelProperty(value = "包装级别")
    private String packLevel;

    @ApiModelProperty(value = "包装名称")
    private String packName;

    @ApiModelProperty(value = "最小包装数")
    private Integer minQty;


    @ApiModelProperty(value = "最大包装数")
    private Integer maxQty;


    @ApiModelProperty(value = "规则模板BO")
    private String ruleMouldBo;

    @ApiModelProperty(value = "规则模板")
    private String ruleMould;


    @ApiModelProperty(value = "包装规则BO")
    private String packRuleBo;


    @ApiModelProperty(value = "包装规则")
    private String packRule;
}
