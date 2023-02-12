package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author chenjx1
 * @date 2021/10/14 17:31
 */
@Data
@ApiModel(value = "排程产线保存2实体")
public class ScheduleProductionLineSaveTwoDTO {

    @ApiModelProperty(value ="排程产线BO")
    private String bo;

    @ApiModelProperty(value ="排程BO")
    private String scheduleBo;

    @ApiModelProperty(value ="车间BO")
    private String workShopBo;

    @ApiModelProperty(value ="车间编号或名称")
    private String workShop;

    @ApiModelProperty(value ="产线BO")
    private String productionLineBo;

    @ApiModelProperty(value ="产线编号")
    private String productionLineCode;

    @ApiModelProperty(value ="产线描述")
    private String productionLineDesc;

    @ApiModelProperty(value ="班次BO")
    private String shiftBo;

    @ApiModelProperty(value ="班次编号")
    private String shiftCode;

    @ApiModelProperty(value ="班次名称")
    private String shiftName;

    @ApiModelProperty(value ="数量")
    private Integer quantity;

    @ApiModelProperty(value ="开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value ="工单号")
    private String shopOrder;

    @ApiModelProperty(value ="任务状态")
    private String state;

}
