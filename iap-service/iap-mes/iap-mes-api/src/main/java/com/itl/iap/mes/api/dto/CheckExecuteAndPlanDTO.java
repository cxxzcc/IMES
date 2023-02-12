package com.itl.iap.mes.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.CheckExecuteItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @Author cjq
 * @Date 2021/12/2 10:26 上午
 * @Description TODO
 */
@Data
@ApiModel("点检工单包含计划")
public class CheckExecuteAndPlanDTO {

    private String id;

    @ApiModelProperty("点检单号")
    private String checkNo;

    @ApiModelProperty("点检计划id")
    private String checkPlanId;

    @ApiModelProperty("点检计划名称")
    private String checkPlanName;

    @ApiModelProperty("设备id")
    private String deviceId;

    @ApiModelProperty(value="设备编号")
    private String device;

    @ApiModelProperty(value="设备名称")
    private String deviceName;

    @ApiModelProperty(value="设备类型")
    private String deviceType;

    @ApiModelProperty("点检状态码")
    private String state;

    @ApiModelProperty("点检状态名称")
    private String stateName;

    @ApiModelProperty("点检开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String startTime;

    @ApiModelProperty("点检结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String endTime;

    @ApiModelProperty("数据收集组id")
    private String dataCollectionId;

    @ApiModelProperty("数据收集组名称")
    private String dataCollectionName;

    @ApiModelProperty("点检人姓名")
    private String checkUserName;

    @ApiModelProperty("操作人姓名")
    private String operaUserName;

}
