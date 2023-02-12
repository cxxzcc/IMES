package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 产品检验项目
 *
 * @author cch
 * @email ${email}
 * @date 2021-10-18 16:14:54
 */
@Data
@TableName("me_product_inspection_items")
@ApiModel(value = "me_product_inspection_items", description = "comments")
public class MeProductInspectionItemsEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("createDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @ApiModelProperty("创建日期")
    private Date createDate;
    @TableField("createUser")
    @ApiModelProperty("创建人")
    private String createUser;
    /**
     * 工序
     */
    @TableField("operationBo")
    @ApiModelProperty("工序")
    private String operationBo;

    /**
     * 工序编号
     */
    @TableField("operation")
    private String operation;

    /**
     * 产品编码
     */
    @TableField("itemBo")
    @ApiModelProperty("产品编码")
    private String itemBo;
    @TableField(exist = false)
    private String item;
    @TableField(exist = false)
    private String itemName;
    /**
     * 单位
     */
    @TableField("unitBo")
    @ApiModelProperty("单位")
    private String unitBo;
    @TableField(exist = false)
    private String unit;
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
    @ApiModelProperty("检验标识 0-定性 1-定量")
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
     * 规格下限判断符号  > =0  >= = 1
     */
    @TableField("lower_limit_symbol")
    @ApiModelProperty("规格下限判断符号   > =0  >= = 1")
    private String lowerLimitSymbol;
    /**
     * 规格下限
     */
    @TableField("lower_limit")
    @ApiModelProperty("规格下限")
    private String lowerLimit;
    /**
     * 规格上限判断符号  < =0  <= = 1
     */
    @TableField("upper_limit_symbol")
    @ApiModelProperty("规格上限判断符号    < =0  <= = 1")
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
     * $column.comments
     */
    @TableId(type = IdType.AUTO)
    @TableField("ID")
    private Integer id;


    /**
     * 工厂
     * */
    @TableField("site")
    @ApiModelProperty("工厂")
    private String site;

}
