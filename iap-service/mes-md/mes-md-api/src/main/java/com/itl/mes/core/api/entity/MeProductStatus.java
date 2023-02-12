package com.itl.mes.core.api.entity;

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
    @ApiModelProperty("productName")
    private String productName;

    @TableField("current_person")
    @ApiModelProperty("currentPerson")
    private String currentPerson;

    @TableField("current_d")
    @ApiModelProperty("currentD")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date currentD;

    @TableField("done")
    @ApiModelProperty("done")
    private Integer done;

    @TableField("on_line")
    @ApiModelProperty("online")
    private Integer online;
    @TableField("hold")
    @ApiModelProperty("hold")
    private Integer hold;
    @TableField("state")
    @ApiModelProperty("状态 0=关闭， 1=正常")
    private Integer state;


    @TableField(exist = false)
    private String itemBo;
    @TableField(exist = false)
    private String shopOrder;
    @TableField(exist = false)
    private String snState;


}
