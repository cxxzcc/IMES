package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 产品维修记录
 * @author dengou
 * @date 2021/11/11
 */
@Data
@TableName("m_production_maintenance_record")
public class ProductMaintenanceRecord {
    
    
    /**
    * id
    * */
    @TableId(type = IdType.UUID) 
    @ApiModelProperty(value = "id")
    private String id;
    /**
    * 缺陷代码
    * */
    @TableField("defect_code") 
    @ApiModelProperty("缺陷代码")
    private String defectCode;
    /**
    *  返修时间
    * */
    @TableField("repair_time") 
    @ApiModelProperty("返修时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date repairTime;
    /**
    * 返修人 username
    * */
    @TableField("repair_user") 
    @ApiModelProperty("返修人 username")
    private String repairUser;
    /**
    * 产线编号
    * */
    @TableField("production_line") 
    @ApiModelProperty("产线编号")
    private String productionLine;
    /**
    * 班次
    * */
    @TableField("classes") 
    @ApiModelProperty("班次")
    private String classes;
    /**
    * 采集记录id
     * {@link ProductionDefectRecord#getId()}
    * */
    @TableField("collection_record_id") 
    @ApiModelProperty("采集记录id")
    private String collectionRecordId;


    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "创建人用户名")
    private String createUser;

    /**
     * 维修措施列表
     * */
    @TableField(exist = false)
    private List<ProductMaintenanceMethodRecord> productMaintenanceMethodRecords;
    
}
