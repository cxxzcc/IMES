package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @author FengRR
 * @date 2021/5/26
 * @since JDK1.8
 */
@Data
@TableName("p_pack_rule")
@ApiModel(value="PackingRule",description="包装规则")
public class PackingRule extends Model<PackingRule> {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty(value="PKR:SITE,PACK_NAME【PKR】")
    @TableId(value = "BO", type = IdType.INPUT)
    private String bo;

    @ApiModelProperty(value="工厂")
    @TableField("SITE")
    private String site;

    @ApiModelProperty(value="包装规则")
    @TableField("PACK_RULE")
    private String packRule;

    @ApiModelProperty(value="包装规则描述")
    @TableField("PACK_RULE_DESC")
    private String packRuleDesc;

    @ApiModelProperty(value="物料类型 1-原材料 2-半成品 3-成品")
    @TableField("ITEM_TYPE")
    private String itemType;

    @ApiModelProperty(value="状态  0-禁用 1-启用")
    @TableField("STATUS")
    private String status;

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

