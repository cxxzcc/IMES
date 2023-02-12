package com.itl.mom.label.api.dto.label;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author cch
 * @date 2021/11/4$
 * @since JDK1.8
 */
@ApiModel(value = "SnChangeDto")
@Data
public class SnChangeDto {

    @ApiModelProperty(value = "sn")
    private String sn;
    @ApiModelProperty(value = "shopOrder")
    private String shopOrder;
}
