package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 生产采集记录-关键件
 * @author dengou
 * @date 2021/11/11
 */
@Accessors(chain = true)
@Data
@TableName("m_production_collection_record_key")
public class ProductionCollectionRecordKey {

    /**
     * id
     * */
    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;
    /**
     * 物料标签
     * */
    @TableField("item_label")
    @ApiModelProperty("物料标签")
    private String itemLabel;
    /**
     * 物料批次
     * */
    @TableField("item_lot")
    @ApiModelProperty("物料批次")
    private String itemLot;
    /**
     * 用料数
     * */
    @TableField("use_number")
    @ApiModelProperty("用料数")
    private Integer useNumber;
    /**
     * 物料编码
     * */
    @TableField("item_code")
    @ApiModelProperty("物料编码")
    private String itemCode;
    /**
     * 物料名称
     * */
    @TableField("item_name")
    @ApiModelProperty("物料名称")
    private String itemName;
    /**
     * 物料描述
     * */
    @TableField("item_desc")
    @ApiModelProperty("物料描述")
    private String itemDesc;
    /**
     *单位
     * */
    @TableField("unit")
    @ApiModelProperty("单位")
    private String unit;
    /**
     * 操作时间
     * */
    @TableField("operation_time")
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date operationTime;
    /**
     * 生产采集记录Id
     * {@link ProductionCollectionRecord#getId()}
     * */
    @TableField("production_collection_record_id")
    @ApiModelProperty("生产采集记录Id")
    private String productionCollectionRecordId;


    /**
     * 工位 编号
     * */
    @TableField(exist = false)
    @ApiModelProperty("工位 编号")
    private String station;
    /**
     * 工序 编号
     * */
    @TableField(exist = false)
    @ApiModelProperty("工序 编号")
    private String process;

}
