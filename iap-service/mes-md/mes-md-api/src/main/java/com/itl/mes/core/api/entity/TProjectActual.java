package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
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
@TableName("t_project_actual")
@ApiModel("项目标准值实体")
public class TProjectActual extends BaseEntity {

    @ApiModelProperty("项目code")
    @TableId
    private String device_bo;

    @ApiModelProperty("项目code")
    @TableId
    private String project_code;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableId
    private Date use_date;

    @ApiModelProperty("标准值")
    @TableField("standard")
    private BigDecimal standard;

    @ApiModelProperty("实际值")
    @TableField(value = "actual", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual;

    @ApiModelProperty("实际值1")
    @TableField(value = "actual1", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual1;

    @ApiModelProperty("误差")
    @TableField("range")
    private BigDecimal range;


    @ApiModelProperty("结果-合格/不合格")
    @TableField("result")
    private String result;

    @ApiModelProperty("实际值2")
    @TableField(value = "actual2", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual2;

    @ApiModelProperty("实际值3")
    @TableField(value = "actual3", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual3;

    @ApiModelProperty("实际值4")
    @TableField(value = "actual4", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual4;

    @ApiModelProperty("实际值5")
    @TableField(value = "actual5", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual5;

    @ApiModelProperty("实际值6")
    @TableField(value = "actual6", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual6;

    @ApiModelProperty("实际值7")
    @TableField(value = "actual7", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual7;

    @ApiModelProperty("实际值8")
    @TableField(value = "actual8", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual8;

    @ApiModelProperty("实际值9")
    @TableField(value = "actual9", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual9;

    @ApiModelProperty("实际值10")
    @TableField(value = "actual10", strategy = FieldStrategy.IGNORED)
    private BigDecimal actual10;


}
