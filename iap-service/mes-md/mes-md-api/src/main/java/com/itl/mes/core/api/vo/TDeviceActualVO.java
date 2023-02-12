package com.itl.mes.core.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@ApiModel("仪器标准值返回对象")
public class TDeviceActualVO {

    @ApiModelProperty("仪器bo")
    private String deviceBo;

    @ApiModelProperty("仪器编码")
    private String device;

    @ApiModelProperty("仪器名称")
    private String deviceName;

    @ApiModelProperty("仪器位置")
    private String baseLocation;

    @ApiModelProperty("是否基准仪器")
    private String isStandard;

    @ApiModelProperty("使用日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date useDate;

    @ApiModelProperty("标准值")
    private BigDecimal standard;

    @ApiModelProperty("实际值1")
    private BigDecimal actual1;

    @ApiModelProperty("误差-日常")
    private BigDecimal range;

    @ApiModelProperty("结果-日常")
    private String result;

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





}
