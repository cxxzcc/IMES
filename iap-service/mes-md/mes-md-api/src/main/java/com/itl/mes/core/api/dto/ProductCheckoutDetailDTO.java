package com.itl.mes.core.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "产品检验数据集")
public class ProductCheckoutDetailDTO {

    @QueryWapper(value = "a.site", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "工厂")
    private String site;

    @QueryWapper(value = "a.work_order_number", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "工单")
    private String workOrderNumber;

    @QueryWapper(value = "b.project_id", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "项目id")
    private String code;

    @QueryWapper(value = "b.result", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "是否合格")
    private String sfhg;



}
