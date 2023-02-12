package com.itl.mes.pp.api.dto.schedule;

import com.baomidou.mybatisplus.annotation.TableField;
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
@ApiModel(value = "排程保存2请求实体")
public class ScheduleSaveTwoDTO {


    @ApiModelProperty(value = "排程BO，主键")
    private String bo;

    @ApiModelProperty(value = "排程号")
    private String scheduleNo;

    @ApiModelProperty(value ="生产订单ID")
    private String orderId;

    @ApiModelProperty(value = "生产订单编号")
    private String orderNo;

    @ApiModelProperty(value = "生产订单描述")
    private String orderDesc;

    @ApiModelProperty(value = "订单类型")
    private String orderType;

    @ApiModelProperty(value ="物料BO")
    private String itemBo;

    @ApiModelProperty(value ="物料编码")
    private String itemCode;

    @ApiModelProperty(value ="物料名称")
    private String itemName;

    @ApiModelProperty(value ="物料版本号")
    private String itemVersion;

    @ApiModelProperty(value ="排程数量")
    private BigDecimal quantity;

    @ApiModelProperty(value ="排程类型")
    private String scheduleType;

    @ApiModelProperty(value = "排程状态，1为创建，2为下达，3为接收")
    private Integer state;

    @ApiModelProperty(value ="控制状态")
    private Integer controlState;

    @ApiModelProperty(value ="开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date startDate;

    @ApiModelProperty(value ="结束时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date endDate;


    @ApiModelProperty(value ="工厂")
    private String site;

    @ApiModelProperty(value ="排程号编码规则类型")
    private String codeRuleType;

    private String schedulePlineBo;

    @ApiModelProperty(value ="产能分配")
    private List<ScheduleProductionLineSaveTwoDTO> scheduleProductionLineSaveTwoDTOList;

    @ApiModelProperty(value ="工单号集合")
    private List<String> shopOrders;

}
