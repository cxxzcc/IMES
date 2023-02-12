package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 校验sn条码请求实体
 *
 * @author GKL
 * @date 2021/11/4 - 16:41
 * @since 2021/11/4 - 16:41 星期四 by GKL
 */
@Data
@ApiModel(value = "CheckSnBindDto", description = "校验sn条码请求实体")
public class CheckSnBindDto {
    @ApiModelProperty(value = "工位")
    private String station;
    @ApiModelProperty(value = "sn条码")
    private String sn;

    @ApiModelProperty(value = "工厂")
    private String site;

}
