package com.itl.mes.core.api.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 产线工艺路线设置表
 */
@Data
@TableName("process_route_fit_production_line")
public class ProcessRouteFitProductionLine {

    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;

    @Excel(name = "工艺路线编码", orderNum = "2")
    @ApiModelProperty("工艺路线编码")
    @TableField("route_code")
    private String routeCode;

    @Excel(name = "产线编码", orderNum = "1")
    @ApiModelProperty("产线编码")
    @TableField("product_line")
    private String productLine;

    @Excel(name = "类型", orderNum = "0")
    @ApiModelProperty("工单类型")
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty("工厂")
    @TableField("site")
    private String site;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField("create_date")
    private Date createDate;
}
