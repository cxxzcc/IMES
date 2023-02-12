package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 定性/定量检验暂存信息 dto
 * @Classname QualitativeInspection
 * @Description TODO
 * @Date 2021/11/1 16:10
 * @author 侯凡
 */
@Data
@ApiModel(value = "qualitativeInspectionSaveDTO",description = "定性/定量检验暂存信息")
public class QualitativeInspectionSaveDTO {

    @ApiModelProperty(name = "条形码", dataType = "string", required = true)
    private String sn;

    @ApiModelProperty(name = "检验项目Id")
    private String projectId;

    @ApiModelProperty(name = "检验项目名称", dataType = "string")
    private String projectName;

    @ApiModelProperty(name = "规范下限")
    private String lowerLimit;

    @ApiModelProperty(name = "规范上限")
    private String upperLimit;

    @ApiModelProperty(name = "测试值")
    private String test;

    @ApiModelProperty(name = "检验结果")
    private String result;

    @ApiModelProperty(name = "检验人")
    private String surveyor;

    @ApiModelProperty(name = "缺陷信息列表")
    private List<DefCodeInfoDto> defCodeList;

    @ApiModelProperty(name = "工位编码(bo)", dataType = "string")
    private String stationBo;

    @ApiModelProperty(name = "车间")
    private String workShop;

    @ApiModelProperty(name = "工位")
    private String station;

}
