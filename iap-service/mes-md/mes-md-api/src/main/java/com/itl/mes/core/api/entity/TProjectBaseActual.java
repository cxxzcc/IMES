package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("t_project_base_actual")
@ApiModel("项目标准值实体")
public class TProjectBaseActual extends BaseEntity {

    @ApiModelProperty("项目code")
    @TableId
    private String project_code;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableId
    private Date use_date;

    @ApiModelProperty("基地code")
    @TableId
    private String base_code;

    @ApiModelProperty("标准值")
    @TableField("standard")
    private BigDecimal standard;

    @ApiModelProperty("实际值")
    @TableField("actual")
    private BigDecimal actual;

    @ApiModelProperty("误差")
    @TableField("range")
    private BigDecimal range;






}
