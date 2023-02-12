package com.itl.iap.system.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel(value = "菜单移动参数")
public class SysResourceMoveDTO {

    @NotEmpty
    @ApiModelProperty(value = "资源id")
    private List<String> idList;

    @NotBlank
    @ApiModelProperty(value = "父菜单id")
    private String parentId;

}
