package com.itl.iap.mes.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 工单执行情况统计dto
 * @author dengou
 * @date 2021/10/21
 */
@Data
public class RepairExecuteCountStatisticsDTO {

    /**
     * 日期
     * */
    @ApiModelProperty(value="日期")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date date;

    /**
     * 工单总数
     * */
    @ApiModelProperty(value="工单总数")
    private Integer totalCount;

    /**
     * 已执行工单数
     * */
    @ApiModelProperty(value="已执行工单数")
    private Integer executeCount;


}
