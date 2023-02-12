package com.itl.iap.mes.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 维修记录
 * @author dengou
 * @date 2021/9/27
 */
@Data
public class RepairRecordDTO {


    /**
     * 维修工单Id
     * */
    @ApiModelProperty(value="维修工单id")
    private String id;

    /**
     * 工单编号
     * */
    @ApiModelProperty(value="工单编号")
    private String repairNo;
    /**
     * 异常描述
     * */
    @ApiModelProperty(value="异常描述")
    private String remark;
    /**
     * 维修员
     * */
    @ApiModelProperty(value="维修员")
    private String planRepairUserName;
    /**
     * 处理方法
     * */
    @ApiModelProperty(value="处理方法")
    private String repairProcrssDesc;

    /**
     * 异常代码
     * */
    @ApiModelProperty(value="异常代码")
    private String faultCode;

    /**
     * 报修时间
     * */
    @ApiModelProperty(value="报修时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date happenTime;

    /**
     * 维修开始时间
     */
    @ApiModelProperty(value="维修开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date repairStartTime;

    /**
     * 维修结束时间
     */
    @ApiModelProperty(value="维修结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date repairEndTime;

}
