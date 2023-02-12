package com.itl.iap.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "系统参数保存对象")
public class IapSysParameterSaveDTO {

    private String id;

    @NotBlank
    @ApiModelProperty(value="参数编码")
    private String code;

    @NotBlank
    @ApiModelProperty(value="参数名称")
    private String name;

    @NotBlank
    @ApiModelProperty(value="字典项id")
    private String dictItemId;

    @NotBlank
    @ApiModelProperty(value="默认值")
    private String defaultValue;

    @ApiModelProperty(value="显示名称")
    private String showValue;

    @ApiModelProperty(value="描述")
    private String remark;


}
