package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author dengou
 * @date 2021/12/13
 */
@Data
public class ProveDto {

    @ApiModelProperty(value = "id")
    private String proveId;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "状态")
    private Integer state;

    @ApiModelProperty(value = "编号")
    private String proveCode;

    @ApiModelProperty(value = "描述")
    private String proveDescription;
}
