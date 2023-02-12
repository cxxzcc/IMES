package com.itl.plugins.report.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("查询对象")
public class ReportContentQueryDTO {

    private Page page;

    @QueryWapper(queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("编码")
    private String code;

    @QueryWapper(queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty("名称")
    private String name;


}
