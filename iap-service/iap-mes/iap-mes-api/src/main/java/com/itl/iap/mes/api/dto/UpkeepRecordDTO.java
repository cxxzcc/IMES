package com.itl.iap.mes.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 保养记录
 * @author dengou
 * @date 2021/9/27
 */
@Data
public class UpkeepRecordDTO {

    /**
     * 保养记录id
     * */
    private String id;
    /**
     * 保养工单编号
     * */
    @ApiModelProperty(value="保养工单编号")
    private String upkeepNo;
    /**
     * 描述
     * */
    @ApiModelProperty(value="描述")
    private String remark;
    /**
     * 保养人姓名
     * */
    @ApiModelProperty(value="保养人姓名")
    private String upkeepUserName;

    /**
     * 保养开始时间
     * */
    @ApiModelProperty("保养开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date startTime;

    /**
     * 保养结束时间
     * */
    @ApiModelProperty("保养结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date endTime;

    /**
     * 数据收集组名称
     * */
    @ApiModelProperty("数据收集组名称")
    private String dataCollectionName;
    /**
     * 数据收集组id
     * */
    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

}
