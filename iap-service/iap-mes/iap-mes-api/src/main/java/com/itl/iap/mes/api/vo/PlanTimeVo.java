package com.itl.iap.mes.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @auth liuchenghao
 * @date 2020/12/22
 */
@Data
@ApiModel(value = "PlanTimeVo", description = "计划生成的工单执行时间")
public class PlanTimeVo {

    @ApiModelProperty(value = "工单编码")
    private String executeNo;

    @ApiModelProperty(value = "开始时间")
    private String startTime;

    @ApiModelProperty(value = "结束时间")
    private String endTime;


}
