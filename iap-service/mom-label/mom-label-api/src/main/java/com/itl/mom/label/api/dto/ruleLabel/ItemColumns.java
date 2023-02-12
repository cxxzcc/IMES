package com.itl.mom.label.api.dto.ruleLabel;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yaoxiang
 * @date 2021/1/21
 * @since JDK1.8
 */
@Data
@ApiModel("元素(物料/工单/设备/容器)字段")
public class ItemColumns {
    @ApiModelProperty("字段")
    private String columnName;
    @ApiModelProperty("字段Label")
    private String columnLabel;
}
