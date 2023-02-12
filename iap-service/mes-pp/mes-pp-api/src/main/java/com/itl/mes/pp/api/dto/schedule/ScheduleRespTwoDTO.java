package com.itl.mes.pp.api.dto.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenjx1
 * @date 2021/10/14 16:53
 */
@Data
@ApiModel(value = "排程查询返回实体")
public class ScheduleRespTwoDTO {

    @ApiModelProperty(value = "排程BO，主键")
    private String bo;

    @ApiModelProperty(value = "排程编号")
    private String scheduleNo;

    @ApiModelProperty(value ="订单ID")
    private String orderId;

    @ApiModelProperty(value = "订单编号")
    private String orderNo;

    @ApiModelProperty(value ="排程类型")
    private String scheduleType;

    @ApiModelProperty(value = "排程状态，1为创建，2为下达，3为接收")
    private Integer state;

    @ApiModelProperty(value ="控制状态")
    private Integer controlState;

    @ApiModelProperty(value ="优先级")
    private String priority;

    @ApiModelProperty(value ="排程总数量")
    private BigDecimal quantityTotal;

    @ApiModelProperty(value ="工厂号")
    private String site;

    @ApiModelProperty(value ="创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;

    @ApiModelProperty(value ="创建用户")
    private String createUser;


    @ApiModelProperty(value ="物料ID")
    private String itemBo;

    @ApiModelProperty(value ="物料编号")
    private String itemCode;

    @ApiModelProperty(value ="物料名称")
    private String itemName;

    @ApiModelProperty(value ="物料版本号")
    private String itemVersion;

    @ApiModelProperty(value ="排程号编码规则类型")
    private String codeRuleType;

    @ApiModelProperty(value ="排程产能ID")
    private String schedulePlineBo;

    /*@ApiModelProperty(value ="产能分配")
    private List<ScheduleProductionLineSaveTwoDTO> scheduleProductionLineSaveTwoDTOList;*/

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
    private BigDecimal quantity;

    @ApiModelProperty(value ="开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;

    @ApiModelProperty(value = "任务状态，ADV_S1未下达，ADV_S2已下达")
    private String taskStateKey;

    @ApiModelProperty(value ="工单号")
    private String shopOrder;

    @ApiModelProperty(value ="工单号集合，用于查询单条排程信息回显工单号")
    private List<String> shopOrders;

}
