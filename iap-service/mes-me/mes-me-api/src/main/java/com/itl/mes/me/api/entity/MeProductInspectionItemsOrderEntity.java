package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.entity.MeProductInspectionItemsOrderNcCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 产品检验项目-工单
 *
 * @author chenjx1
 * @date 2021-10-20
 */
@Data
@TableName("me_product_inspection_items_order")
@ApiModel(value = "me_product_inspection_items_order", description = "comments")
public class MeProductInspectionItemsOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 工序
     */
    @TableField("operationBo")
    @ApiModelProperty("工序")
    private String operationBo;

    @TableField("operation")
    @ApiModelProperty("工序编号")
    private String operation;

    /**
     * 工单ID
     */
    @TableField("orderBo")
    @ApiModelProperty("工单ID")
    private String orderBo;

    /**
     * 产品物料检验类型（0产品、1产品组）
     */
    @TableField("item_type")
    @ApiModelProperty("产品物料检验类型（0产品、1产品组）")
    private String itemType;

    /**
     * 产品组编号
     */
    @TableField("itemGroupBo")
    @ApiModelProperty("产品组编号")
    private String itemGroupBo;

    /**
     * 产品编码
     */
    @TableField("itemBo")
    @ApiModelProperty("产品编码")
    private String itemBo;
    /**
     * 单位
     */
    @TableField("unitBo")
    @ApiModelProperty("单位")
    private String unitBo;
    /**
     * 检验项目
     */
    @TableField("check_project")
    @ApiModelProperty("检验项目")
    private String checkProject;
    /**
     * 检验类别
     */
    @TableField("check_category")
    @ApiModelProperty("检验类别")
    private String checkCategory;
    /**
     * 检验工具
     */
    @TableField("check_tools")
    @ApiModelProperty("检验工具")
    private String checkTools;
    /**
     * 检验标识
     */
    @TableField("check_mark")
    @ApiModelProperty("检验标识")
    private String checkMark;
    /**
     * 检验类型
     */
    @TableField("check_type")
    @ApiModelProperty("检验类型")
    private String checkType;
    /**
     * 检验依据
     */
    @TableField("check_basis")
    @ApiModelProperty("检验依据")
    private String checkBasis;
    /**
     * 缺陷等级
     */
    @TableField("defect_levels")
    @ApiModelProperty("缺陷等级")
    private String defectLevels;
    /**
     * 技术要求
     */
    @TableField("technical_requirements")
    @ApiModelProperty("技术要求")
    private String technicalRequirements;
    /**
     * 规格下限判断符号
     */
    @TableField("lower_limit_symbol")
    @ApiModelProperty("规格下限判断符号")
    private String lowerLimitSymbol;
    /**
     * 规格下限
     */
    @TableField("lower_limit")
    @ApiModelProperty("规格下限")
    private String lowerLimit;
    /**
     * 规格上限判断符号
     */
    @TableField("upper_limit_symbol")
    @ApiModelProperty("规格上限判断符号")
    private String upperLimitSymbol;
    /**
     * 规格上限
     */
    @TableField("upper_limit")
    @ApiModelProperty("规格上限")
    private String upperLimit;
    /**
     * 生效日期
     */
    @TableField("effective_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("生效日期")
    private Date effectiveDate;
    /**
     * 失效日期
     */
    @TableField("expiry_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("失效日期")
    private Date expiryDate;

    /**
     * 创建日期
     */
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("创建日期")
    private Date createDate;
    /**
     * 创建用户
     */
    @ApiModelProperty(value = "建档人")
    @TableField("create_user")
    private String createUser;
    /**
     * 修改日期
     */
    @ApiModelProperty(value = "修改日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField("modify_date")
    private Date modifyDate;
    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    @TableField("modify_user")
    private String modifyUser;

    /**
     * $column.comments
     */
    @TableId(type = IdType.AUTO)
    @TableField("ID")
    private Integer id;

    @ApiModelProperty(value = "不良代码集合副本")
    @TableField(exist = false)
    List<MeProductInspectionItemsOrderNcCode> meProductInspectionItemsOrderNcCodeList;

}
