package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author GKL
 * @date 2021/11/29 - 15:00
 * @since 2021/11/29 - 15:00 星期一 by GKL
 */
@Data
@ApiModel(value = "BomItemSnByStation", description = "工位对应的物料条码数据")

public class BomItemSnByStation {
    @ApiModelProperty(value = "工位")
    private String station;
    @ApiModelProperty(value = "工位对应的已上料数据")
    private List<UsedListDto> usedList;
}
