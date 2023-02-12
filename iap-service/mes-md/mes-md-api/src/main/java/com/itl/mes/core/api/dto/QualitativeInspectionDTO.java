package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Classname QualitativeInspection
 * @Description TODO
 * @Date 2021/11/1 16:10
 * @author 侯凡
 */
@Data
@ApiModel(value = "qualitativeInspectionDTO",description = "工位定性检验")
public class QualitativeInspectionDTO {

    @ApiModelProperty(name = "条形码", dataType = "string", required = true)
    private String sn;

    @ApiModelProperty(name = "工位编码(bo)", dataType = "string", required = true)
    private String stationBo;

    @ApiModelProperty(name = "工厂", dataType = "string", required = true)
    private String site;
}
