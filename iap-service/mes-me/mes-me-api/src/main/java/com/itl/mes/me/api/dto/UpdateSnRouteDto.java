package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 更新条码工艺路线dto
 * @author dengou
 * @date 2021/11/26
 */
@Data
public class UpdateSnRouteDto {

    /**
     * 条码列表
     * */
    @ApiModelProperty(value = "条码列表")
    private List<String> snList;

    /**
     * 工艺路线详细信息
     * */
    @ApiModelProperty(value = "工艺路线详细信息")
    private String processInfo;

    /**
     * 工厂
     * */
    @ApiModelProperty(value = "工厂")
    private String site;
}
