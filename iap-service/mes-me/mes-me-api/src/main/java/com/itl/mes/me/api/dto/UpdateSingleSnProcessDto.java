package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 更新sn当前工序 dto
 * @author dengou
 * @date 2021/11/29
 */
@Data
public class UpdateSingleSnProcessDto {

    @ApiModelProperty(value = "sn")
    private String sn;
    @ApiModelProperty(value = "工艺路线json的工序id")
    private String processId;
}
