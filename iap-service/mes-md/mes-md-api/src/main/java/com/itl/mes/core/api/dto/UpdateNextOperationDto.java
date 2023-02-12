package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 批量更新下工序dto
 * @author dengou
 * @date 2021/11/29
 */
@Data
public class UpdateNextOperationDto {

    /**
     * snBo列表
     * */
    @ApiModelProperty(value = "snBo列表")
    private List<String> snBoList;
    /**
     * 工序bo
     * */
    @ApiModelProperty(value = "工序bo")
    private String operationBo;
    /**
     * 工序名称
     * */
    @ApiModelProperty(value = "工序名称")
    private String operationName;
}
