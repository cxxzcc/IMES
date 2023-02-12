package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 修改是否完工dto
 *
 * @author GKL
 * @date 2021/11/16 - 17:02
 * @since 2021/11/16 - 17:02 星期二 by GKL
 */
@Data
@ApiModel(value = "UpdateDoneDto", description = "修改是否完工dto")
public class UpdateDoneDto {
    private String productStateBo;
    private Integer done;
}
