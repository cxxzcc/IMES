package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@ApiModel(value = "仪器类型查询对象")
public class TInstrumentTypeQueryDTO {

    private Page page;

    @QueryWapper(queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("名称")
    private String name;



}
