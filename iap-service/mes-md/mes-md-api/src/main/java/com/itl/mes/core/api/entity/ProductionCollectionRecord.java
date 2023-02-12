package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.mes.core.api.constant.ProductionCollectionRecordStateEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 生产采集记录
 * @author dengou
 * @date 2021/11/11
 */
@Accessors(chain = true)
@Data
@TableName("m_production_collection_record")
public class ProductionCollectionRecord {

    /**
     * id
     * */
    @TableId(type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;
    /**
     * 工位 编号
     * */
    @TableField("station")
    @ApiModelProperty("工位 编号")
    private String station;
    /**
     * 工序 编号
     * */
    @TableField("process")
    @ApiModelProperty("工序 编号")
    private String process;
    /**
     * 产线 编号
     * */
    @TableField("production_line")
    @ApiModelProperty("产线 编号")
    private String productionLine;
    /**
     * 班次
     * */
    @TableField("classes")
    @ApiModelProperty("班次")
    private String classes;
    /**
     * 班次
     * */
    @TableField("classes_id")
    @ApiModelProperty("班次id")
    private String classesId;
    /**
     * 采集结果
     * */
    @TableField("result")
    @ApiModelProperty("采集结果")
    private String result;
    /**
     * 状态
     * {@link ProductionCollectionRecordStateEnum#getCode()}
     * */
    @TableField("state")
    @ApiModelProperty("状态, 1=完成")
    private Integer state;
    /**
     * 状态
     * {@link ProductionCollectionRecordStateEnum#getDesc()}
     * */
    @TableField(exist = false)
    @ApiModelProperty("状态描述")
    private String stateDesc;
    /**
     * 操作时间
     * */
    @TableField("operation_time")
    @ApiModelProperty("操作时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date operationTime;
    /**
     * 采集记录主键
     * {@link CollectionRecord#id}
     * */
    @TableField("collection_record_id")
    @ApiModelProperty("采集记录id")
    private String collectionRecordId;

    /**
     * 关键件列表
     * */
    @TableField(exist = false)
    private List<ProductionCollectionRecordKey> productionCollectionRecordKeys;
}
