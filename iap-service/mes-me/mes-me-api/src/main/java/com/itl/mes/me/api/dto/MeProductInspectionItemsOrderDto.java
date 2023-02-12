package com.itl.mes.me.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenjx1
 * @date 2021/10/20
 * @since JDK1.8
 */
@Data
@ApiModel(value = "MeProductInspectionItemsOrderDto",description = "产品检验项目-工单副本")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MeProductInspectionItemsOrderDto {


    /**
     * 产品检验项目-工单副本id
     * */
    private Integer id;
    @ApiModelProperty("工单ID")
    private String orderBo;
    @ApiModelProperty("产品编号/产品编号组")
    private String itemType;
    @ApiModelProperty("产品编码")
    private String itemBo;
    @ApiModelProperty("产品组编码")
    private String itemGroupBo;
    @ApiModelProperty("工序")
    private String operationBo;
    @ApiModelProperty("检验项目")
    private String checkProject;
    @ApiModelProperty("检验标识")
    private String checkMark;
    @ApiModelProperty("检验类型")
    private String checkType;
    @ApiModelProperty("生效日期")
    private Date effectiveDateStart;
    @ApiModelProperty("失效日期")
    private Date expiryDateStart;
    @ApiModelProperty("生效日期")
    private Date effectiveDateEnd;
    @ApiModelProperty("失效日期")
    private Date expiryDateEnd;
    @ApiModelProperty("创建日期")
    private Date createDate;
    @ApiModelProperty("创建用户")
    private String createUser;
    @ApiModelProperty("修改日期")
    private Date modifyDate;
    @ApiModelProperty("修改用户")
    private String modifyUser;

    /**
     * 检验类别
     */
    @ApiModelProperty("检验类别")
    private String checkCategory;
    /**
     * 检验工具
     */
    @ApiModelProperty("检验工具")
    private String checkTools;
    /**
     * 检验依据
     */
    @ApiModelProperty("检验依据")
    private String checkBasis;
    private Page page;


    /**
     * 规格下限
     */
    @ApiModelProperty("规格下限")
    private String lowerLimit;
    /**
     * 规格上限
     */
    @ApiModelProperty("规格上限")
    private String upperLimit;
    /**
     * 规格下限判断符号
     */
    @ApiModelProperty("规格下限判断符号")
    private String lowerLimitSymbol;
    /**
     * 规格上限判断符号
     */
    @ApiModelProperty("规格上限判断符号")
    private String upperLimitSymbol;

    /**
     * 检验测试值
     * */
    private String test = "";
}
