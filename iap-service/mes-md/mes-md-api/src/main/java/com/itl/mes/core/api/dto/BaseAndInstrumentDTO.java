package com.itl.mes.core.api.dto;

import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@ApiModel(value = "基地查询对象")
public class BaseAndInstrumentDTO {

    @QueryWapper(value = "base_id", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("基地id")
    private String baseId;

    @QueryWapper(value = "instrument_type_id", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("仪器类型id")
    private String instrumentTypeId;


}
