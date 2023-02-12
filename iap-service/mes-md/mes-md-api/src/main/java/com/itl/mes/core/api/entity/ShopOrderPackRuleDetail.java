package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Date;

/**
 * @auth liuchenghao
 * @date 2021/5/28
 *
 * 工单包装规则明细表
 */
@Data
@TableName("p_shop_order_pack_rule_detail")
@ApiModel(value = "ShopOrderPackRuleDetail",description = "工单包装规则明细表")
public class ShopOrderPackRuleDetail {


    @ApiModelProperty(value="主键")
    @TableId(value = "BO", type = IdType.UUID)
    private String bo;

    @ApiModelProperty(value="序号")
    @TableField("SEQ")
    private String seq;

    @ApiModelProperty(value="包装级别")
    @TableField("PACK_LEVEL")
    private String packLevel;

    @ApiModelProperty(value="包装名称")
    @TableField("PACK_NAME")
    private String packName;

    @ApiModelProperty(value="最大包装数")
    @TableField("MAX_QTY")
    @Min(value = 0,message="最大包装数不能为负值")
    private Integer maxQty;

    @ApiModelProperty(value="最小包装数")
    @TableField("MIN_QTY")
    @Min(value = 0,message="最小包装数不能为负值")
    private Integer minQty;

    @ApiModelProperty(value="规则模板bo")
    @TableField("RULE_MOULD_BO")
    private String ruleMouldBo;

    @ApiModelProperty(value="工厂")
    @TableField("SITE")
    private String site;

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

    @ApiModelProperty(value="包装规则bo")
    @TableField("PACK_RULE_BO")
    private String packRuleBo;

    @ApiModelProperty(value="工单BO")
    @TableField("SHOP_ORDER_BO")
    private String shopOrderBo;

}
