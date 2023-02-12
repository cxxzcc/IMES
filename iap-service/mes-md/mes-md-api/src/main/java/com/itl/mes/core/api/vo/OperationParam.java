package com.itl.mes.core.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 工序参数vo
 * @author dengou
 * @date 2021/10/25
 */
@Data
public class OperationParam {

    /**
     * 参数名称
     * */
    @ApiModelProperty( value = "参数名称" )
    private String name;
    /**
     * 参数key
     * */
    @ApiModelProperty( value = "参数key" )
    private String key;

    /**
     * 参数描述
     * */
    @ApiModelProperty( value = "参数描述" )
    private String desc;

}
