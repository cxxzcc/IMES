package com.itl.mes.me.api.dto.snLifeCycle;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author 崔翀赫
 * @date 2021/1/27$
 * @since JDK1.8
 */
@Data
@ApiModel("基本信息")
@Accessors(chain = true)
public class BasicInfo {


    @ApiModelProperty(value = "工厂")
    private String site;
    @ApiModelProperty(value = "车间")
    private String workShopBo;
    @ApiModelProperty(value = "产线")
    private String productLineBo;
    @ApiModelProperty(value = "当前工序")
    private String operationBo;
    @ApiModelProperty(value = "当前工位")
    private String stationBo;
    @ApiModelProperty(value = "sn状态")
    private String state;
    @ApiModelProperty(value = "排程")
    private String schedule;
    @ApiModelProperty(value = "工单")
    private String shopOrder;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "排程计划时间")
    private Date sPlanTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "排程完成时间")
    private Date sEndTime;

    @ApiModelProperty(value = "工单数量")
    private Integer releaseQty;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "计划时间")
    private Date planEndDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "完成时间")
    private Date orderDeliveryTime;

    @ApiModelProperty(value = "排程数量")
    private String quantity;
    @ApiModelProperty(value = "工步状态")
    private String state2;
}
