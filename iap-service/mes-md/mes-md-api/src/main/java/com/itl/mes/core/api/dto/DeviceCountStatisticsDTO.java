package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 设备数量统计dto
 * @author dengou
 * @date 2021/10/20
 */
@Data
public class DeviceCountStatisticsDTO {


    /**
     * 状态
     * */
    @ApiModelProperty("状态code")
    private String state;

    /**
     * 状态说明
     * */
    @ApiModelProperty("状态说明")
    private String stateDesc;

    /**
     * 数量 
     * */
    @ApiModelProperty("数量")
    private Integer count;

}
