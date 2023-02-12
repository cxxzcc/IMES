package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 产品工艺路线设置表-中间表
 */
@Data
@TableName("process_route_fit_product")
@ApiModel("产品工艺路线设置表-中间表")
public class ProcessRouteFitProduct {

    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;

//    @ApiModelProperty("工艺路线BO")
//    @TableField("route_bo")
//    private String routeBo;

    @Excel(name = "工艺路线编码", orderNum = "2")
    @ApiModelProperty(value = "工艺路线编码", required = true)
    @TableField("route_code")
    private String routeCode;

//    @ApiModelProperty("工艺路线名称")
//    @TableField("route_name")
//    private String routeName;

//    @ApiModelProperty("物料BO")
//    @TableField("item_bo")
//    private String itemBo;

    @Excel(name = "产品编码", orderNum = "1")
    @ApiModelProperty(value = "物料编码", required = true)
    @TableField("item_code")
    private String itemCode;

    @Excel(name = "类型", orderNum = "0")
    @ApiModelProperty(value = "类型: (试产:trial  量产:normal  返工:rework)", required = true)
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("工厂")
    @TableField("site")
    private String site;

    @ApiModelProperty("创建时间")
    @TableField("create_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

}
