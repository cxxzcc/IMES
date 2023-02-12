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
@ApiModel(value = "各基地对标保存对象")
public class TProjectBaseActualDTO {


    @NotBlank
    @ApiModelProperty("项目编码")
    private String projectCode;

    @NotBlank
    @ApiModelProperty("基地编码")
    private String baseCode;

    @NotNull
    @ApiModelProperty("使用日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date useDate;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("实际值")
    private BigDecimal actual;

    @Digits(integer = 6, fraction = 4, message = "整数位最多6位,小数位最多4位")
    @ApiModelProperty("误差")
    private BigDecimal range;




}
