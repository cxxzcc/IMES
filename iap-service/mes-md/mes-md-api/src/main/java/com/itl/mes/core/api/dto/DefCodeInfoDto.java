package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 不合格代码信息dto
 * @author dengou
 * @date 2021/11/15
 */
@Data
public class DefCodeInfoDto {



    @ApiModelProperty(name = "缺陷记录")
    private String defectRecord;

    @ApiModelProperty(name = "缺陷代码")
    private String defectCode;

    @ApiModelProperty(name = "缺陷描述")
    private String defectDescription;

    @ApiModelProperty(name = "检验项目")
    private String descriptionOfInspectionItems;

}
