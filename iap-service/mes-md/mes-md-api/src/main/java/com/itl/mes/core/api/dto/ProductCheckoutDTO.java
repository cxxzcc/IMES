package com.itl.mes.core.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "产品检验数据集")
public class ProductCheckoutDTO {

    @QueryWapper(value = "a.site", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "工厂")
    private String site;

    @QueryWapper(value = "a.workshop", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "车间")
    private String workshop;

    @QueryWapper(value = "a.production_line", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "产线")
    private String productionLine;

    @QueryWapper(value = "a.work_order_number", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "工单")
    private String workOrderNumber;

    @QueryWapper(value = "b.process", queryWapperEnum = QueryWapperEnum.LIKE)
    @ApiModelProperty(value = "工序")
    private String process;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;

}
