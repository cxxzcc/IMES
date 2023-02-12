package com.itl.iap.mes.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * app首页统计dto
 * @author dengou
 * @date 2021/9/30
 */
@Data
public class IndexStatisticsDTO {

    /**
     * 设备总数
     * */
    @ApiModelProperty("设备总数")
    private Integer deviceCount;
    /**
     * 异常总数
     * */
    @ApiModelProperty("异常总数")
    private Integer abnormalCount;
    /**
     * 报修工单数
     * */
    @ApiModelProperty("报修工单数")
    private Integer repairCount;
    /**
     * 保养工单数
     * */
    @ApiModelProperty("保养工单数")
    private Integer upkeepCount;
    /**
     * 点检工单数
     * */
    @ApiModelProperty("点检工单数")
    private Integer checkCount;


}
