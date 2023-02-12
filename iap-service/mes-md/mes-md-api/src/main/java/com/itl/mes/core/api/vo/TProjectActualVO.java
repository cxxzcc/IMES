package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("项目标准值返回对象")
public class TProjectActualVO {

    @ApiModelProperty("基地编码")
    private String baseCode;

    @ApiModelProperty("基地名称")
    private String baseName;

    @ApiModelProperty("仪器类型名称")
    private String instrumentTypeName;

    @ApiModelProperty("仪器编码")
    private String device;

    @ApiModelProperty("仪器名称")
    private String deviceName;

    @ApiModelProperty("仪器位置")
    private String baseLocation;

    @ApiModelProperty("项目编码")
    private String projectCode;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date useDate;

    @ApiModelProperty("标准值")
    private BigDecimal standard;

    @ApiModelProperty("实际值")
    private BigDecimal actual;

    @ApiModelProperty("实际值1")
    private BigDecimal actual1;

    @ApiModelProperty("误差")
    private BigDecimal range;

    @ApiModelProperty("导入用户")
    private String createBy;

    @ApiModelProperty("导入时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("实际值2")
    private BigDecimal actual2;

    @ApiModelProperty("实际值3")
    private BigDecimal actual3;

    @ApiModelProperty("实际值4")
    private BigDecimal actual4;

    @ApiModelProperty("实际值5")
    private BigDecimal actual5;

    @ApiModelProperty("实际值6")
    private BigDecimal actual6;

    @ApiModelProperty("实际值7")
    private BigDecimal actual7;

    @ApiModelProperty("实际值8")
    private BigDecimal actual8;

    @ApiModelProperty("实际值9")
    private BigDecimal actual9;

    @ApiModelProperty("实际值10")
    private BigDecimal actual10;

    @ApiModelProperty("偏差")
    private String standardDeviation;

    @ApiModelProperty("单位")
    private String unit;

    @ApiModelProperty("小数位")
    private Integer digital;


}
