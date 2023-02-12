package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 维修暂存数据
 * @author dengou
 * @date 2021/11/22
 */
@Data
public class RepairTempDataDTO {


    @ApiModelProperty("采集记录id")
    private String collectionRecordId;

    @ApiModelProperty("缺陷记录id")
    private String defectRecordId;

    @ApiModelProperty(value = "工序 编号")
    private String process;

    @ApiModelProperty(value = "工厂")
    private String site;

    @ApiModelProperty(value = "工位 编号")
    private String station;

    @ApiModelProperty(value = "缺陷记录")
    private String defectRecords;

    @ApiModelProperty(value = "缺陷代码")
    private String defectCode;

    @ApiModelProperty(value = "缺陷描述")
    private String defectDescription;

    @ApiModelProperty(value = "检验项目描述")
    private String descriptionOfInspectionItems;

    @NotNull(message = "维修措施编码不能为空")
    private String code;

    @NotNull(message = "维修措施名称不能为空")
    private String title;

    @ApiModelProperty("维修措施说明")
    private String description;

    @ApiModelProperty("异常类型id")
    private String errorTypeId;

    @ApiModelProperty("维修方法")
    private String method;

    @ApiModelProperty("维修位置")
    private String place;

    @ApiModelProperty("备注")
    private String remark;


    @ApiModelProperty("维修人")
    private String repairUserName;
    @ApiModelProperty("班次")
    private String shiftName;
    @ApiModelProperty("产线编号")
    private String productionLine;




}
