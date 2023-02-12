package com.itl.iap.mes.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.iap.mes.api.common.OperateTypeEnum;
import com.itl.iap.mes.api.common.OrderTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 工单操作记录
 *
 * @author dengou
 * @date 2021/10/25
 */
@Data
@TableName("m_repair_operate_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RepairOperateLog {

    /**
     * id
     */
    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty("id")
    private String id;

    /**
     * 订单id
     */
    @TableField(value = "ORDER_ID")
    @ApiModelProperty("订单id")
    private String orderId;

    @TableField(value = "DEVICE_ID")
    @ApiModelProperty("设备ID")
    private String deviceId;

    @TableField(value = "DEVICE_CODE")
    @ApiModelProperty("设备编码")
    private String deviceCode;

    /**
     * 订单编号
     */
    @TableField(value = "ORDER_NO")
    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 操作人id
     */
    @TableField(value = "OPERATE_USER_ID")
    @ApiModelProperty("操作人id")
    private String operateUserId;
    /**
     * 操作人名称
     */
    @TableField(value = "OPERATE_USER_NAME")
    @ApiModelProperty("操作人名称")
    private String operateUserName;
    /**
     * 操作时间
     */
    @TableField(value = "OPERATE_TIME")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @ApiModelProperty("操作时间")
    private Date operateTime;
    /**
     * 工厂id
     */
    @TableField(value = "SITE")
    @ApiModelProperty("工厂id")
    private String site;
    /**
     * 订单类型
     * {@link OperateTypeEnum#getCode()}
     */
    @TableField(value = "ORDER_TYPE")
    @ApiModelProperty("订单类型")
    private String orderType;
    /**
     * 订单类型说明
     * {@link OperateTypeEnum#getDesc()}
     */
    @TableField("ORDER_TYPE_DESC")
    @ApiModelProperty("订单类型说明")
    private String orderTypeDesc;
    /**
     * 操作类型
     * {@link OrderTypeEnum#getCode()}
     */
    @TableField(value = "OPERATE_TYPE")
    @ApiModelProperty("操作类型")
    private String operateType;
    /**
     * 操作类型说明
     * {@link OrderTypeEnum#getCode()}
     */
    @TableField(exist = false)
    @ApiModelProperty("操作类型说明")
    private String operateTypeDesc;
    /**
     * 附加数据
     */
    @TableField("EXTRA_DATA")
    @ApiModelProperty(value = "附加数据", hidden = true)
    private String extraData;
    /**
     * 操作人手机号码
     */
    @TableField(exist = false)
    @ApiModelProperty(value = "操作人手机号码")
    private String phone;

    public RepairOperateLog() {
    }

    @Builder
    public RepairOperateLog(String id, String orderId, String orderNo, String deviceId, String deviceCode,
                            String operateUserId, String operateUserName, Date operateTime,
                            String site, String orderType,String orderTypeDesc, String operateType, String extraData) {
        this.id = id;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.deviceId = deviceId;
        this.deviceCode = deviceCode;
        this.operateUserId = operateUserId;
        this.operateUserName = operateUserName;
        this.operateTime = operateTime;
        this.site = site;
        this.orderType = orderType;
        this.orderTypeDesc = orderTypeDesc;
        this.operateType = operateType;
        this.extraData = extraData;
    }
}
