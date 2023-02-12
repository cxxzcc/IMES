package com.itl.mom.label.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * product_status(MeProductStatus)表实体类
 *
 * @author makejava
 * @since 2021-10-22 11:09:32
 */
@Data
@TableName("me_product_status")
@ApiModel(value = "me_product_status", description = "comments")
@Accessors(chain = true)
public class MeProductStatus extends Model<MeProductStatus> {

    @TableId(type = IdType.UUID)
    private String id;

    @TableField("sn_bo")
    @ApiModelProperty("snBo")
    private String snBo;

    @TableField("current_operation")
    @ApiModelProperty("currentOperation")
    private String currentOperation;
    @TableField("current_operation_id")
    @ApiModelProperty("currentOperationId")
    private String currentOperationId;

    @TableField("next_operation")
    @ApiModelProperty("nextOperation")
    private String nextOperation;
    @TableField("next_operation_id")
    @ApiModelProperty("nextOperationId")
    private String nextOperationId;

    @TableField("current_pl_station")
    @ApiModelProperty("currentPlStation")
    private String currentPlStation;

    @TableField("product_name")
    @ApiModelProperty("产品名称")
    private String productName;
    @TableField("product_code")
    @ApiModelProperty("产品编码")
    private String productCode;

    @TableField("current_person")
    @ApiModelProperty("currentPerson")
    private String currentPerson;

    @TableField("current_d")
    @ApiModelProperty("currentD")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentD;

    @TableField("done")
    @ApiModelProperty("是否完工, 0=否，1=是")
    private Integer done;

    @TableField("on_line")
    @ApiModelProperty("online")
    private Integer online;
    @TableField("hold")
    @ApiModelProperty("是否挂起，0=否,1=是")
    private Integer hold;
    @TableField("state")
    @ApiModelProperty("状态 1=正常,0=关闭")
    private Integer state;
    /**
     * 条码对应工单编号
     * */
    @TableField("shop_order")
    @ApiModelProperty("工单编号")
    private String shopOrder;

    /**
     * 物料bo
     * */
    @TableField(exist = false)
    private String itemBo;
    /**
     * 条码状态 z_sn.state
     * */
    @TableField(exist = false)
    private String snState;


}
