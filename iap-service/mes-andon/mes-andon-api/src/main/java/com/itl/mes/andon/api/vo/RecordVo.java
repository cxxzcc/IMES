package com.itl.mes.andon.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @auth liuchenghao
 * @date 2020/12/24
 */
@Data
@ApiModel(value = "RecordVo", description = "异常日志信息返回实体")
public class RecordVo implements Serializable {

    @ApiModelProperty(value = "触发人")
    private String triggerMan;

    @ApiModelProperty(value = "触发时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date triggerTime;

    @ApiModelProperty(value = "设备故障代码BO")
    private String faultCodeBo;

    @ApiModelProperty(value = "设备故障代码")
    private String faultCode;

    @ApiModelProperty(value = "叫料数量")
    private BigDecimal callQuantity;

    @ApiModelProperty(value = "异常图片")
    private String abnormalImg;

    @ApiModelProperty(value = "异常备注")
    private String abnormalRemark;

    @ApiModelProperty(value = "设备编号")
    private String code;

    @ApiModelProperty(value = "物料编号")
    private String item;

    @ApiModelProperty(value = "异常图片,base64文件")
    private List<String> imgs;

    @ApiModelProperty("呼叫人账号(,号拼接的)")
    private String callMan;

    @ApiModelProperty("呼叫人姓名(,号拼接的)")
    private String callManName;

    @ApiModelProperty(value = "触发等级(与安灯等级不同)")
    private String urgencyLevel;

    @ApiModelProperty(value = "维修单号")
    private String repairNo;

    @ApiModelProperty(value = "状态 (1异常 2修复)")
    private String state;
}
