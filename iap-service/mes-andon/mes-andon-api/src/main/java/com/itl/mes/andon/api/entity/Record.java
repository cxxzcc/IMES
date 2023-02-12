package com.itl.mes.andon.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 安灯日志
 *
 * @author cuichonghe
 * @date 2020-12-14 14:56:55
 */
@Data
@TableName("andon_record")
@ApiModel(value = "record", description = "安灯日志")
public class Record implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键【PK】
     */
    @ApiModelProperty(value = "主键【PK】")
    @Length(max = 100)
    @TableId(value = "pid", type = IdType.UUID)
    private String pid;

    /**
     * 工厂
     */
    @ApiModelProperty(value = "工厂")
    @TableField("SITE")
    private String site;
    /**
     * 安灯
     */
    @ApiModelProperty(value = "安灯")
    @TableField("ANDON_BO")
    private String andonBo;
    /**
     * 资源类型
     */
    @ApiModelProperty(value = "资源类型")
    @TableField("RESOURCE_TYPE")
    private String resourceType;
    /**
     * 车间
     */
    @ApiModelProperty(value = "车间")
    @TableField("WORK_SHOP_BO")
    private String workShopBo;
    /**
     * 产线
     */
    @ApiModelProperty(value = "产线")
    @TableField("PRODUCT_LINE_BO")
    private String productLineBo;
    /**
     * 工位
     */
    @ApiModelProperty(value = "工位")
    @TableField("STATION_BO")
    private String stationBo;
    /**
     * 设备
     */
    @ApiModelProperty(value = "设备")
    @TableField("DEVICE_BO")
    private String deviceBo;
    /**
     * 异常时间
     */
    @ApiModelProperty(value = "异常时间")
    @TableField("ABNORMAL_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date abnormalTime;

    /**
     * 设备故障代码(设备异常时使用)
     */
    @ApiModelProperty(value = "设备故障代码BO(设备异常时使用)")
    @TableField("FAULT_CODE_BO")
    private String faultCodeBo;

    @ApiModelProperty(value = "设备故障代码(设备异常时使用)")
    @TableField("FAULT_CODE")
    private String faultCode;

    /**
     * 叫料数量
     */
    @ApiModelProperty(value = "叫料数量")
    @TableField("CALL_QUANTITY")
    private BigDecimal callQuantity;
    /**
     * 异常图片
     */
    @ApiModelProperty(value = "异常图片")
    @TableField("ABNORMAL_IMG")
    private String abnormalImg;

    /**
     * 异常备注
     */
    @ApiModelProperty(value = "问题描述")
    @TableField("ABNORMAL_REMARK")
    private String abnormalRemark;

    /**
     * 修复时间
     */
    @ApiModelProperty(value = "维修时间")
    @TableField("REPAIR_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date repairTime;

    /**
     * 修复人
     */
    @ApiModelProperty(value = "维修人(账号,号拼接)")
    @TableField("REPAIR_MAN")
    private String repairMan;

    @ApiModelProperty(value = "触发时间")
    @TableField("TRIGGER_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date triggerTime;

    @ApiModelProperty(value = "触发人")
    @TableField("TRIGGER_MAN")
    private String triggerMan;

    @ApiModelProperty(value = "受理时间")
    @TableField("RECEIVE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    @ApiModelProperty(value = "受理人员")
    @TableField("RECEIVE_MAN")
    private String receiveMan;

    /**
     * 修复图片
     */
    @ApiModelProperty(value = "修复图片")
    @TableField("REPAIR_IMG")
    private String repairImg;
    /**
     * 处理方法
     */
    @ApiModelProperty(value = "处理方法")
    @TableField("REPAIR_REMARK")
    private String repairRemark;
    /**
     * 状态
     */
    @ApiModelProperty(value = "状态 (1异常 2修复)")
    @TableField("STATE")
    private String state;

    @ApiModelProperty(value = "物料")
    @TableField("ITEM_BO")
    private String itemBo;

    @ApiModelProperty(value = "呼叫人员,多个人员以,号拼接 取:(userName用户账号)")
    @TableField("CALL_MAN")
    private String callMan;

//    private String callManName;

    @ApiModelProperty(value = "解除时间")
    @TableField("CLOSE_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime;

    @ApiModelProperty(value = "解除人员")
    @TableField("CLOSE_MAN")
    private String closeMan;

    @ApiModelProperty(value = "触发等级(与安灯等级不同)")
    @TableField("URGENCY_LEVEL")
    private String urgencyLevel;

    @ApiModelProperty(value = "计划修复时间")
    @TableField("PLAN_REPAIR_TIME")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date planRepairTime;

    @ApiModelProperty(value = "报障持续时间")
    @TableField("ABNORMAL_WORK_HOUR")
    private Double abnormalWorkHour;

    @ApiModelProperty(value = "报障响应时间")
    @TableField(exist = false)
    private Double abnormalWorkMin;

    @ApiModelProperty(value = "安灯的等级")
    @TableField(exist = false)
    private String level;

    @ApiModelProperty(value = "影响的工单")
    @TableField("AFFECT_SHOP_ORDER")
    private String affectShopOrder;

    @ApiModelProperty(value = "维修单号")
    @TableField("REPAIR_NO")
    private String repairNo;

    @ApiModelProperty(value = "分页对象")
    @TableField(exist = false)
    private Page page;

    @ApiModelProperty(value = "安灯编号")
    @TableField(exist = false)
    private String andon;

    @ApiModelProperty(value = "安灯名称")
    @TableField(exist = false)
    private String andonName;

    @ApiModelProperty(value = "安灯描述")
    @TableField(exist = false)
    private String andonDesc;

    @ApiModelProperty(value = "安灯类型")
    @TableField(exist = false)
    private String andonType;

    @ApiModelProperty(value = "安灯等级")
    @TableField(exist = false)
    private String andonGrade;

    // 安灯触发到受理的时间段
    @ApiModelProperty(value = "安灯报障响应时间(触发到受理)")
    @TableField(exist = false)
    private String receivePeriodTime;

    // 安灯触发到维修结束的时间端
    @ApiModelProperty(value = "安灯报障持续时间(触发到维修完成)")
    @TableField(exist = false)
    private String completePeriodTime;

}
