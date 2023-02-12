package com.itl.plugins.report.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("保存对象")
public class ReportContentDTO {

    private String id;

    @NotBlank
    @ApiModelProperty("编码")
    private String code;

    @NotBlank
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;


}
