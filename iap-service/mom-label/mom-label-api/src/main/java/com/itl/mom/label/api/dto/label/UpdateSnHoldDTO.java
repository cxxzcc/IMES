package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 更新sn是否挂起
 * @author dengou
 * @date 2021/11/29
 */
@Data
public class UpdateSnHoldDTO {

    /**
     * 产品状态id列表
     * */
    @ApiModelProperty(value = "产品状态id列表， 和snBoList二选一")
    private List<String> idList;
    /**
     * 条码列表
     * */
    @ApiModelProperty(value = "条码列表， 和idList二选一")
    private List<String> snBoList;

    /**
     * 是否挂起 0=否, 1=是
     * */
    @ApiModelProperty(value = "是否挂起 0=否, 1=是")
    private Integer hold;
}
