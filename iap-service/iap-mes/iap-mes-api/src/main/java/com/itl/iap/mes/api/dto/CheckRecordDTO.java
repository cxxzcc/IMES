package com.itl.iap.mes.api.dto;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 点检记录dto
 * @author dengou
 * @date 2021/9/28
 */
@Data
public class CheckRecordDTO {


    /**
     * 点检记录id
     * */
    @ApiModelProperty(value="点检记录id", hidden = true)
    private String id;
    /**
     * 工单编号
     * */
    @ApiModelProperty(value="工单编号")
    private String checkNo;

    /**
     * 现场照片列表
     * */
    @ApiModelProperty(value="现场照片列表")
    private List<String> imgs;

    /**
     * 点检人
     * */
    @ApiModelProperty(value="点检人")
    private String checkUserName;

    /**
     * 点检日期
     * */
    @ApiModelProperty(value="点检日期")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    private Date checkDate;

    /**
     * 数据收集组名称
     * */
    @ApiModelProperty("数据收集组名称")
    private String dataCollectionName;
    /**
     * 数据收集组名称
     * */
    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    /**
     * 点检开始时间
     * */
    @ApiModelProperty("点检开始时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date startTime;

    /**
     * 点检结束时间
     * */
    @ApiModelProperty("点检结束时间")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date endTime;


}
