package com.itl.iap.mes.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.CheckDevice;
import com.itl.iap.mes.api.entity.CheckPlanUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;


@Data
@ApiModel("设备点检计划保存对象")
public class CheckPlanDto {

    @ApiModelProperty("点检计划id")
    private String id;

    @ApiModelProperty("点检计划编号")
    private String planNo;

    @ApiModelProperty("点检计划名称")
    private String checkPlanName;

    @ApiModelProperty("设备列表")
    private List<CheckDevice> devices;

    @ApiModelProperty("点检人列表")
    private List<CheckPlanUser> checkUsers;

    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @ApiModelProperty("开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    @ApiModelProperty("间隔  每多长时间执行")
    private Integer cycle;

    @ApiModelProperty("月 天 时  的常量  0月1天2时")
    private Integer ytd;

    @ApiModelProperty("描述")
    private String remark;


}
