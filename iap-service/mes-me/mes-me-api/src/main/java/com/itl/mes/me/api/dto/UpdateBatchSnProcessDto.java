package com.itl.mes.me.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 更新条码当前工序dto
 * @author dengou
 * @date 2021/11/26
 */
@Data
public class UpdateBatchSnProcessDto {

    /**
     * 条码列表
     * */
    @ApiModelProperty(value = "条码列表")
    private List<String> snList;

    /**
     * 工序bo
     * */
    @ApiModelProperty(value = "工序bo")
    private String operationBo;
}
