package com.itl.iap.mes.api.dto.sparepart;

import cn.hutool.core.date.DatePattern;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 更换记录vo
 * @author dengou
 * @date 2021/9/23
 */
@Data
public class DeviceChangeRecordDTO {

    /**
     * 设备编号
     * */
    @ApiModelProperty(value="设备编号")
    private String device;
    /**
     * 设备名称
     * */
    @ApiModelProperty(value="设备名称")
    private String deviceName;
    /**
     * 规格型号
     * */
    @ApiModelProperty(value="规格型号")
    private String deviceModel;
    /**
     * 类型
     * */
    @ApiModelProperty(value="类型")
    private String type;
    /**
     * 关联单号
     * */
    @ApiModelProperty(value="关联单号")
    private String referenceOrderNo;
    /**
     * 时间
     * */
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value="时间")
    private Date changeDate;
    /**
     * 数量
     * */
    @ApiModelProperty(value="数量")
    private Integer count;
}
