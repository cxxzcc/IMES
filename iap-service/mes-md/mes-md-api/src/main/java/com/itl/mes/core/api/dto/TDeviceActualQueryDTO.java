package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itl.iap.common.base.mapper.QueryWapper;
import com.itl.iap.common.base.mapper.QueryWapperEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("项目标准值查询")
public class TDeviceActualQueryDTO {

    private Page page;

    @ApiModelProperty("开始日期")
    private String startTime;

    @ApiModelProperty("结束日期")
    private String endTime;

    @QueryWapper(value = "b.base_id", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("基地id")
    private String baseId;

    @QueryWapper(value = "b.bo", queryWapperEnum = QueryWapperEnum.EQ)
    @ApiModelProperty("仪器bo")
    private String deviceBo;

}
