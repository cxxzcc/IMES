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
 * 产品组工艺路线
 */
@Data
@TableName("process_route_fit_product_group")
public class ProcessRouteFitProductGroup {

    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;

    @Excel(name = "工艺路线编码", orderNum = "2")
    @ApiModelProperty("工艺路线编码")
    @TableField("route_code")
    private String routeCode;

    @Excel(name = "产品组编码", orderNum = "1")
    @ApiModelProperty("物料组编码")
    @TableField("item_group")
    private String itemGroup;

    @ApiModelProperty("工厂")
    @TableField("site")
    private String site;

    @Excel(name = "类型", orderNum = "0")
    @ApiModelProperty("工单类型")
    @TableField("order_type")
    private String orderType;

    @ApiModelProperty("创建人")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("创建时间")
    @TableField("create_date")
    private Date createDate;

}
