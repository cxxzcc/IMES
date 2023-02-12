package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 维修记录-维修措施
 * @author dengou
 * @date 2021/11/11
 */
@Data
@TableName("m_production_maintenance_method_record")
public class ProductMaintenanceMethodRecord {

    /**
     * 主键id
     * */
    @TableId(type = IdType.UUID)
    @ApiModelProperty("主键id")
    private String id;
    /**
     * 维修编码
     * */
    @TableField("maintenance_code")
    @ApiModelProperty("维修编码")
    private String maintenanceCode;
    /**
     * 维修措施名称
     * */
    @TableField("maintenance_name")
    @ApiModelProperty("维修措施名称")
    private String maintenanceName;
    /**
     * 维修措施描述
     * */
    @TableField("maintenance_desc")
    @ApiModelProperty("维修措施描述")
    private String maintenanceDesc;
    /**
     * 维修记录id
     * {@link ProductMaintenanceRecord#getId()}
     * */
    @TableField("production_maintenance_record_id")
    @ApiModelProperty("维修记录id")
    private String productionMaintenanceRecordId;

}
