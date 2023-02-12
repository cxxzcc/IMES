package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Data
@FieldNameConstants
@ApiModel(value = "各基地对标保存对象")
public class TDeviceActualDTO {


    @NotBlank
    @ApiModelProperty("仪器bo")
    private String deviceBo;

    @NotBlank
    @ApiModelProperty("使用日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date useDate;

    @ApiModelProperty("测试值1")
    private BigDecimal actual1;

    @ApiModelProperty("误差")
    private BigDecimal range;

    @ApiModelProperty("结果")
    private String result;


}
