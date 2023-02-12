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
@TableName("t_device_actual")
@ApiModel("仪器标准值实体")
public class TDeviceActual extends BaseEntity {

    @ApiModelProperty("仪器bo")
    @TableId
    private String device_bo;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableId
    private Date use_date;

    @ApiModelProperty("基准值")
    @TableField("standard")
    private BigDecimal standard;

    @ApiModelProperty("误差-日常")
    @TableField("range")
    private BigDecimal range;

    @ApiModelProperty("结果-日常")
    @TableField("result")
    private String result;

    @ApiModelProperty("实际值1")
    @TableField("actual1")
    private BigDecimal actual1;

    @ApiModelProperty("实际值2")
    @TableField("actual2")
    private BigDecimal actual2;

    @ApiModelProperty("实际值3")
    @TableField("actual3")
    private BigDecimal actual3;

    @ApiModelProperty("实际值4")
    @TableField("actual4")
    private BigDecimal actual4;

    @ApiModelProperty("实际值5")
    @TableField("actual5")
    private BigDecimal actual5;

    @ApiModelProperty("实际值6")
    @TableField("actual6")
    private BigDecimal actual6;

    @ApiModelProperty("实际值7")
    @TableField("actual7")
    private BigDecimal actual7;

    @ApiModelProperty("实际值8")
    @TableField("actual8")
    private BigDecimal actual8;

    @ApiModelProperty("实际值9")
    @TableField("actual9")
    private BigDecimal actual9;

    @ApiModelProperty("实际值10")
    @TableField("actual10")
    private BigDecimal actual10;


}
