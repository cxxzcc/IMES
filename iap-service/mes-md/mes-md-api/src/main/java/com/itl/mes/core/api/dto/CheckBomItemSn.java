package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 校验物料条码上料下料的数据
 *
 * @author GKL
 * @date 2021/11/29 - 14:16
 * @since 2021/11/29 - 14:16 星期一 by GKL
 */
@Data
@ApiModel(value = "CheckBomItemSn", description = "校验物料条码上料下料的数据")
public class CheckBomItemSn {
    @ApiModelProperty(value = "上料,下料状态标志:0上料1下料")
    private Integer state;

    @ApiModelProperty(value = "上料/下料物料条码多个用;拼接")
    private String itemSn;


    @ApiModelProperty(value = "工位")
    private String station;

    @ApiModelProperty(value = "工厂")
    private String site;

    /*@ApiModelProperty(value ="产品条码bo")
    private String snBo;*/


}
