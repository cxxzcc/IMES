package com.itl.iap.mes.api.entity.sparepart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 物料出入库详情
 * @author dengou
 * @date 2021/9/17
 */
@TableName("m_spare_part_storage_record_detail")
@Data
public class SparePartStorageRecordDetail {

    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty(hidden = true)
    private String id;
    /**
     * 出入库记录id
     * */
    @TableField("RECORD_ID")
    @ApiModelProperty(hidden = true)
    private String recordId;
    /**
     * 备件id
     * */
    @TableField("SPARE_PART_ID")
    @ApiModelProperty(value="备件id")
    private String sparePartId;
    /**
     * 入库数量
     * */
    @TableField("IN_COUNT")
    @ApiModelProperty(value="入库数量")
    private Integer inCount;
    /**
     * 出库数量
     * */
    @TableField("OUT_COUNT")
    @ApiModelProperty(value="出库数量")
    private Integer outCount;
    /**
     * 库存
     * */
    @TableField("INVENTORY")
    @ApiModelProperty(value="库存")
    private Integer inventory;
    /**
     * 单价
     * */
    @TableField("PRICE")
    @ApiModelProperty(value="单价")
    private BigDecimal price;
    /**
     * 总金额
     * */
    @TableField("TOTAL_AMOUNT")
    @ApiModelProperty(value="总金额")
    private BigDecimal totalAmount;

    /**
     * 仓库id
     * */
    @TableField("WARE_HOUSE_ID")
    @ApiModelProperty(value="仓库id")
    private String wareHouseId;
    /**
     * 仓库名称
     * */
    @TableField("WARE_HOUSE_NAME")
    @ApiModelProperty(value="仓库名称")
    private String wareHouseName;

    /**
     * 库存变动数量绝对值，用于出入库接口
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="库存变动数量绝对值")
    private Integer count;


    /**
     * 备件编号
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="备件编号")
    private String sparePartNo;
    /**
     * 备件名称
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="备件名称")
    private String sparePartName;

    /**
     * 规格型号
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="规格型号")
    private String spec;

    /**
     * 计量单位
     * */
    @TableField(exist = false)
    @ApiModelProperty(value="计量单位")
    private String unit;


}
