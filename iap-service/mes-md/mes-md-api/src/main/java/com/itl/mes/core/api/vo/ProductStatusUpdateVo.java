package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 产品状态更新
 *
 * @author GKL
 * @date 2021/11/19 - 16:53
 * @since 2021/11/19 - 16:53 星期五 by GKL
 */
@Data
@Accessors(chain = true)
@ApiModel(value="ProductStatusUpdateVo",description="产品状态更新返回参数")
public class ProductStatusUpdateVo {
    @ApiModelProperty(value="工单号")
    private  String shopOrder;
    @ApiModelProperty(value="物料编码")
    private String itemCode;
    @ApiModelProperty(value="物料名称")
    private String itemName;
    @ApiModelProperty(value="排程单号")
    private String scheduleNo;
}
