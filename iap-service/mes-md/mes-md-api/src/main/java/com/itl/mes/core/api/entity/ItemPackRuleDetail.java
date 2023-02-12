package com.itl.mes.core.api.entity;

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
@ApiModel(value="ItemPackRuleDetail",description="物料包装规则明细")
public class ItemPackRuleDetail {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value="主键")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="序号")
    @TableField("SEQ")
    private String seq;

    @ApiModelProperty(value="包装级别")
    @TableField("PACK_LEVEL")
    private String packLevel;

    @ApiModelProperty(value="包装名称")
    @TableField("PACK_NAME")
    private String packName;

    @ApiModelProperty(value="最小包装数")
    @TableField("MIN_QTY")
    private String minQty;

    @ApiModelProperty(value="最大包装数")
    @TableField("MAX_QTY")
    private String maxQty;

    @ApiModelProperty(value="包装规则模板bo")
    @TableField("RULE_MOULD_BO")
    private String ruleMouldBo;

    @ApiModelProperty(value="包装规则模板名")
    @TableField("RULE_MOULD")
    private String ruleMould;

    @ApiModelProperty(value="包装规则bo")
    @TableField("PACK_RULE_BO")
    private String ruleRuleBo;

    @ApiModelProperty(value="物料bo")
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value="创建人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value="创建时间")
    @TableField("CREATE_DATE")
    private Date createDate;

    @ApiModelProperty(value="修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    @ApiModelProperty(value="修改时间")
    @TableField("MODIFY_DATE")
    private Date modifyDate;
}

