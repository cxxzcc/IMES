package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("各基地项目标准值查询")
public class TProjectBaseActualQueryDTO {

    private Page page;

    @ApiModelProperty("开始日期")
    private String startTime;

    @ApiModelProperty("结束日期")
    private String endTime;

    @ApiModelProperty("基地编码或者名称")
    private String baseCodeOrName;

    @ApiModelProperty("项目编码或者名称")
    private String projectCodeOrName;




}
