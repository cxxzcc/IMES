package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author FengRR
 * @date 2021/5/28
 * @since JDK1.8
 */
@Data
@TableName("p_pack_rule_detail_item")
@ApiModel(value="ItemPackRuleDetailDto",description="物料包装规则明细")
public class ItemPackRuleDetailDto {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键")
    private String bo;

    @ApiModelProperty(value="工厂")
    private String site;

    @ApiModelProperty(value="序号")
    private String seq;

    @ApiModelProperty(value="包装级别")
    private String packLevel;

    @ApiModelProperty(value="包装名称")
    private String packName;

    @ApiModelProperty(value="最小包装数")
    private String minQty;

    @ApiModelProperty(value="最大包装数")
    private String maxQty;

    @ApiModelProperty(value="物料编码")
    private String item;

    @ApiModelProperty(value="版本")
    private String version;

    @ApiModelProperty(value="包装规则模板bo")
    private String ruleMouldBo;

    @ApiModelProperty(value="包装规则模板名")
    private String ruleMould;

    @ApiModelProperty(value="包装规则bo")
    private String ruleRuleBo;

    @ApiModelProperty(value="包装规则")
    private String packRule;

    @ApiModelProperty(value="物料bo")
    private String itemBo;

    @ApiModelProperty(value="创建人")
    private String createUser;

    @ApiModelProperty(value="创建时间")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    private Date modifyDate;
}

