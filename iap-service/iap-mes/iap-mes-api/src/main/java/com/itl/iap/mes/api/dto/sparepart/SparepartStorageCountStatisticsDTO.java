package com.itl.iap.mes.api.dto.sparepart;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 备件库存统计信息
 * @author dengou
 * @date 2021/9/26
 */
@Data
public class SparepartStorageCountStatisticsDTO {

    /**
     * 库存不足
     * */
    @ApiModelProperty(value="库存不足数量")
    private Integer leCount;

    /**
     * 库存过量
     * */
    @ApiModelProperty(value="库存过量数量")
    private Integer geCount;
}
