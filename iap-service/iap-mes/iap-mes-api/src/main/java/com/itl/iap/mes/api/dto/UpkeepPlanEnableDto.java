package com.itl.iap.mes.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.itl.iap.mes.api.entity.UpkeepDevice;
import com.itl.iap.mes.api.entity.UpkeepUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


@Data
@ApiModel("设备保养计划启用不启用")
public class UpkeepPlanEnableDto {

    @NotEmpty(message = "计划列表不能为空")
    @ApiModelProperty("保养计划id列表")
    private List<String> idList;

    @NotNull(message = "状态不能为空")
    @ApiModelProperty("启用 1 不启用 0")
    private Integer state;


}
