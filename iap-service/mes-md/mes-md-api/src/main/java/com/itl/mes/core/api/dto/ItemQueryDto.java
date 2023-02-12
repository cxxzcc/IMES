package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author liuchenghao
 * @date 2021/4/13 15:52
 */
@Data
@ApiModel(value = "ItemQueryDto",description = "物料查询DTO")
public class ItemQueryDto {

    @ApiModelProperty(value = "物料编码")
    private String item;

    @ApiModelProperty(value = "物料名称")
    private String itemName;

    @ApiModelProperty(value = "工厂")
    private String site;

}
