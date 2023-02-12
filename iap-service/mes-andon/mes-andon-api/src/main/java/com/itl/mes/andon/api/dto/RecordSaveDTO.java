package com.itl.mes.andon.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/23
 */
@Data
@ApiModel(value = "RecordSaveDTO", description = "")
public class RecordSaveDTO {

    @ApiModelProperty(value = "安灯的资源类型")
    private String resourceType;

    @ApiModelProperty(value = "物料BO，可不传")
    private String itemBo;

    @ApiModelProperty(value = "安灯BO")
    private String andonBo;

    @ApiModelProperty(value = "设备BO，可不传")
    private String deviceBo;

    @ApiModelProperty(value = "故障代码BO")
    private String faultCodeBo;

    @ApiModelProperty(value = "设备故障代码(设备异常时使用)")
    private String faultCode;

    @ApiModelProperty(value = "叫料数量")
    private BigDecimal callQuantity;

    @ApiModelProperty(value = "异常备注")
    private String abnormalRemark;

    @ApiModelProperty(value = "异常图片")
    private String abnormalImg;

    @ApiModelProperty(value = "异常视频")
    private String abnormalVideo;

    @ApiModelProperty(value = "安灯报障-维修单号", required = true)
    private String repairNo;

    @ApiModelProperty(value = "当前工位", required = true)
    private String stationBo;

    @ApiModelProperty(value = "呼叫人员,(用户账号userName以,号拼接)", required = true)
    private String callMan;

    @ApiModelProperty(value = "安灯触发(紧急程度)", required = true)
    private String urgencyLevel;
}
