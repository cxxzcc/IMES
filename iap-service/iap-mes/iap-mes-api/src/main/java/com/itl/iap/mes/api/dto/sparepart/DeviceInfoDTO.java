package com.itl.iap.mes.api.dto.sparepart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 关联设备信息vo
 * @author dengou
 * @date 2021/9/22
 */
@Data
public class DeviceInfoDTO {

    /**
     * 设备id
     * */
    @ApiModelProperty(value="设备id")
    private String id;
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
     * 所在产线
     * */
    @ApiModelProperty(value="所在产线")
    private String productLine;
    /**
     * 设备类型
     * */
    @ApiModelProperty(value="设备类型")
    private String deviceTypeName;
    /**
     * 设备图片
     * */
    @ApiModelProperty(value="设备图片")
    private String imgUrl;
    /**
     * 设备状态
     * */
    @ApiModelProperty(value="设备状态")
    private String statusDesc;
    /**
     * 设备状态
     * */
    @JsonIgnore
    private String statusCode;



}
