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
@ApiModel(value = "项目查询对象")
public class TProjectQueryDTO {

    private Page page;

    @QueryWapper(value = "a.code", queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("编码")
    private String code;

    @QueryWapper(value = "a.name", queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("名称")
    private String name;

    @QueryWapper(value = "b.name", queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("仪器类型名称")
    private String instrumentTypeName;



}
