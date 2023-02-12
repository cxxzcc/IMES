package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

/**
 * <p>
 * 载具表
 * </p>
 *
 * @author space
 * @since 2019-07-23
 */
@Data
@TableName("p_shop_order_pack_sn_real")
@ApiModel(value = "工单包装条码存储", description = "工单包装条码存储")
@FieldNameConstants
public class ShopOrderPackSnReal {

    @TableId(value = "id", type = IdType.INPUT)
    private String id;

    @ApiModelProperty(value = "条码")
    @TableField(value = "sn")
    private String sn;

    @ApiModelProperty(value = "工单bo")
    @TableField(value = "station_bo")
    private String stationBo;

    @ApiModelProperty(value = "工单bo")
    @TableField(value = "shop_order")
    private String shopOrder;

    @ApiModelProperty(value = "包装号")
    @TableField(value = "pack_no")
    private String packNo;

    @ApiModelProperty(value = "扫描时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(value = "create_time")
    private Date createTime;

    @ApiModelProperty(value = "site")
    @TableField(value = "site")
    private String site;


}