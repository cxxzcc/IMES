package com.itl.iap.system.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IapSysParameterQueryDto {

    private Page page;

    @QueryWapper(value = "a.dictItemId", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty(value = "字典项目id")
    private String dictItemId;

    @QueryWapper(value = "a.code", queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty(value = "参数编码")
    private String code;

    @QueryWapper(value = "a.name", queryWapperEnum = QueryWapperEnum.MATCH)
    @ApiModelProperty(value = "参数名称")
    private String name;

}
