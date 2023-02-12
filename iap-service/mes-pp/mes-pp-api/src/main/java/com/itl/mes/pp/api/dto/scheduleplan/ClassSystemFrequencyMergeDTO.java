package com.itl.mes.pp.api.dto.scheduleplan;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "ClassSystemFrequencyMergeDTO",description = "数据库查询班制班次组合实体")
public class ClassSystemFrequencyMergeDTO {

    @ApiModelProperty(value = "分页对象")
    private Page page;

    @ApiModelProperty(value = "班制ID")
    private String classSystemId;

    @ApiModelProperty(value = "班制编号")
    private String classSystemCode;

    @ApiModelProperty(value = "班制名称")
    private String classSystemName;

    @ApiModelProperty(value = "班制是否缺省")
    private Integer classSystemIsDefault;

    @ApiModelProperty(value = "班次ID")
    private String classFrequencyID;

    @ApiModelProperty(value = "班次编号")
    private String classFrequencyCode;

    @ApiModelProperty(value = "班次名称")
    private String classFrequencyName;

    @ApiModelProperty(value = "班次是否跨日，0为否1为是")
    private Integer classFrequencyIsNextDay;

    @ApiModelProperty(value = "开始时间")
    private String startDateStr;

    @ApiModelProperty(value = "结束时间")
    private String endDateStr;

}
