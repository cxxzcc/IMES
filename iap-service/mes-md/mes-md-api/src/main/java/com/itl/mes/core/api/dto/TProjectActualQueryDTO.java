package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目标准值查询")
public class TProjectActualQueryDTO {

    private Page page;

    @QueryWapper(value = "b.use_date", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("使用日期")
    private String useDate;

    @QueryWapper(value = "b.project_code", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("项目code")
    private String projectCode;

    @QueryWapper(value = "c.instrument_type_id", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("仪器类型id")
    private String instrumentTypeId;


}
