package com.itl.iap.mes.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Data
@ApiModel("设备点检计划启用不启用")
public class CheckPlanEnableDto {

    @NotEmpty(message = "计划列表不能为空")
    @ApiModelProperty("点检计划id列表")
    private List<String> idList;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("启用 1 不启用 0")
    private Integer state;


}
