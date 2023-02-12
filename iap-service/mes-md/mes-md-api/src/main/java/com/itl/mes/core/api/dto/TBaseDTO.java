package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@FieldNameConstants
@ApiModel(value = "基地保存对象")
public class TBaseDTO {

    private String id;

    @NotBlank
    @Length(max = 64)
    @ApiModelProperty("基地编码")
    private String code;

    @NotBlank
    @Length(max = 255)
    @ApiModelProperty("基地名称")
    private String name;

    @NotBlank
    @Length(max = 1)
    @ApiModelProperty("是否启用")
    private String isUse;

    @Length(max = 2000)
    @ApiModelProperty("基地描述")
    private String remark;


}
