package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 修改设备状态vo
 * @author dengou
 * @date 2021/10/13
 */
@Data
public class DeviceChangeStateRequestVO {

    /**
     * 设备编码
     * */
    @ApiModelProperty("设备编码")
    private String deviceNo;
    /**
     * 状态
     * */
    @ApiModelProperty("状态")
    private String state;
}
