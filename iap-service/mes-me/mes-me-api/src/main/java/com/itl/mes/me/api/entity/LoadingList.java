package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import lombok.Data;

/**
 * 生产执行-批次条码绑定-上料清单
 *
 * @author cch
 * @date 2021-06-08
 */
@Data
@TableName("me_loading_list")
@ApiModel(value = "me_loading_list", description = "生产执行-批次条码绑定-上料清单")
public class LoadingList implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "ID", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private String id;

    @TableField("SITE")
    @ApiModelProperty(value = "工厂")
    private String site;

    @TableField("SFC")
    @ApiModelProperty(value = "SFC编号")
    private String sfc;

    @TableField("TRACE_METHOD")
    @ApiModelProperty(value = "追溯方式")
    private String traceMethod;

    @TableField("COMPONENT_BO")
    @ApiModelProperty(value = "装配物料Bo")
    private String componentBo;

    @TableField("ASSEMBLED_SN")
    @ApiModelProperty(value = "装配SN")
    private String assembledSn;

    @TableField("REMAINING_QTY")
    @ApiModelProperty(value = "剩余数量")
    private Integer remainingQty;

    @TableField("OPERATION_BO")
    @ApiModelProperty(value = "工序Bo")
    private String operationBo;

    @TableField(exist = false)
    @ApiModelProperty(value = "物料编码")
    private String item;
    @TableField(exist = false)
    @ApiModelProperty(value = "物料描述")
    private String itemDesc;
}
