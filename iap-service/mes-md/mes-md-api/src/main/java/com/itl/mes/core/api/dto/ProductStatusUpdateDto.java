package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 产品状态更新
 *
 * @author GKL
 * @date 2021/11/15 - 17:11
 * @since 2021/11/15 - 17:11 星期一 by GKL
 */
@Data
@ApiModel(value = "ProductStatusUpdateDto", description = "产品状态更新请求参数")
public class ProductStatusUpdateDto {
    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "sn条码")
    private String sn;

}
