package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author cjq
 */
@Data
@FieldNameConstants
@ApiModel(value = "基地对标保存对象")
public class TProjectActualDTO {

    @NotBlank
    @ApiModelProperty("仪器编码")
    private String device;

    @NotBlank
    @ApiModelProperty("项目编码")
    private String projectCode;

    @NotNull
    @ApiModelProperty("使用日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date useDate;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值")
    private BigDecimal actual;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值1")
    private BigDecimal actual1;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("误差")
    private BigDecimal range;

    @ApiModelProperty("结果-合格/不合格")
    private String result;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值2")
    private BigDecimal actual2;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值3")
    private BigDecimal actual3;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值4")
    private BigDecimal actual4;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值5")
    private BigDecimal actual5;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值6")
    private BigDecimal actual6;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值7")
    private BigDecimal actual7;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值8")
    private BigDecimal actual8;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值9")
    private BigDecimal actual9;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值10")
    private BigDecimal actual10;



}
