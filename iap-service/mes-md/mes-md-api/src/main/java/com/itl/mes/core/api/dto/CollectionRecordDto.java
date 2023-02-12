package com.itl.mes.core.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *   采集记录dto
 * </p>
 *
 * @author dengou
 * @since 2021-11-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CollectionRecordDto implements Serializable{

    @ApiModelProperty(value = "主键")
    private String id;

    @ApiModelProperty(value = "条码")
    private String barCode;

    @ApiModelProperty(value = "工单编号")
    private String workOrderNumber;

    @ApiModelProperty(value = "是否Hold 1是 0否 默认0")
    private Integer hold;

    @ApiModelProperty(value = "工单类型")
    private String workOrderType;

    @ApiModelProperty(value = "工单数量")
    private Double workCount;

    @ApiModelProperty(value = "工艺流程名称")
    private String processName;

    @ApiModelProperty(value = "车间")
    private String workshop;

    @ApiModelProperty(value = "产线")
    private String productionLine;

    @ApiModelProperty(value = "产品编码")
    private String productCode;

    @ApiModelProperty(value = "产品名称")
    private String productName;

    @ApiModelProperty(value = "当前工序")
    private String currentProcess;

    @ApiModelProperty(value = "下工序")
    private String nextProcess;

    @ApiModelProperty(value = "是否已完工 1是 0否 默认0")
    private Integer complete;

    /**
     * 工单类型说明
     * */
    @ApiModelProperty(value = "工单类型说明")
    private String workOrderTypeStr;


    @ApiModelProperty(value = "车间名称")
    private String workshopName;

    @ApiModelProperty(value = "产线名称")
    private String productionLineName;


    @ApiModelProperty(value = "当前工序名称")
    private String currentProcessName;

    @ApiModelProperty(value = "下工序名称")
    private String nextProcessName;


}
