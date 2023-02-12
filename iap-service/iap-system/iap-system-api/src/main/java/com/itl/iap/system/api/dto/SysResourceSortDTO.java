package com.itl.iap.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel(value = "菜单排序参数")
public class SysResourceSortDTO {

    @ApiModelProperty(value = "资源id")
    private String id;

    @ApiModelProperty(value = "排序")
    private Short sort;

}
