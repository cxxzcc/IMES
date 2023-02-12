package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@FieldNameConstants
@ApiModel(value = "仪器类型保存对象")
public class TInstrumentTypeDTO {

    private String id;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty("项目名称")
    private String name;



}
