package com.itl.mes.core.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.mes.core.api.constant.ProductionOrderSourceEnum;
import com.itl.mes.core.api.constant.ProductionOrderStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Date;

/**
 * 生产订单
 * @author dengou
 * @date 2021/10/11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("production_order")
public class ProductionOrder extends Model<ProductionOrder> {

    /**
     *  id
     * */
    @TableId(value = "ID", type = IdType.UUID)
    @ApiModelProperty(value = "id")
    private String id;
    /**
     * 工厂
     * */
    @TableField("SITE")
    @ApiModelProperty(value = "工厂")
    private String site;
    /**
     * 订单编号
     * */
    @TableField("ORDER_NO")
    @ApiModelProperty(value = "订单编号")
    private String orderNo;
    /**
     * 物料id
     * */
    @TableField("ITEM_BO")
    @ApiModelProperty(value = "物料bo")
    private String itemBo;
    /**
     * 物料编号
     * */
    @TableField("ITEM_NO")
    @ApiModelProperty(value = "物料编号")
    private String itemNo;
    /**
     * 物料名称
     * */
    @TableField("ITEM_NAME")
    @ApiModelProperty(value = "物料名称")
    private String itemName;
    /**
     * 订单状态
     * @see ProductionOrderStatusEnum#getCode()
     * */
    @TableField("STATUS")
    @ApiModelProperty(value = "订单状态")
    private String status;
    /**
     * 订单状态说明
     * @see ProductionOrderStatusEnum#getDesc()
     * */
    @TableField(exist = false)
    @ApiModelProperty(value = "订单状态说明")
    private String statusDesc;
    /**
     * 订单数量
     * */
    @TableField("QUANTITY")
    @Min(value = 1, message = "订单数量必须大于0")
    @ApiModelProperty(value = "订单数量")
    private Integer quantity;

    /**
     * 已排产数量
     * */
    @TableField("SCHEDULED_QUANTITY")
    @ApiModelProperty(value = "已排产数量")
    private Integer scheduledQuantity;
    /**
     * 完工数量
     * */
    @TableField("COMPLETE_QUANTITY")
    @ApiModelProperty(value = "完工数量")
    private Integer completeQuantity;
    /**
     * 类型
     * */
    @TableField("TYPE")
    @ApiModelProperty(value = "类型")
    private String type;
    /**
     * 类型
     * */
    @TableField(exist = false)
    @ApiModelProperty(value = "类型说明")
    private String typeDesc;
    /**
     * 销售订单编号
     * */
    @TableField("SELL_ORDER_NO")
    @ApiModelProperty(value = "销售订单编号")
    private String sellOrderNo;
    /**
     * 销售订单行
     * */
    @TableField("SELL_ORDER_LINE")
    @ApiModelProperty(value = "销售订单行")
    private String sellOrderLine;
    /**
     * 客户名称
     * */
    @TableField("CUSTOM_NAME")
    @ApiModelProperty(value = "客户名称")
    private String customName;
    /**
     * 客户id
     * */
    @TableField("CUSTOM_ID")
    @ApiModelProperty(value = "客户id")
    private String customId;
    /**
     * 到货日期
     * */
    @TableField("DELIVERY_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "到货日期")
    private Date deliveryDate;
    /**
     * 客户交期
     * */
    @TableField("CUSTOM_DELIVERY_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "客户交期")
    private Date customDeliveryDate;
    /**
     * 工厂交期
     * */
    @TableField("SITE_DELIVERY_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "工厂交期")
    private Date siteDeliveryDate;
    /**
     * 齐料日期
     * */
    @TableField("ITEM_READY_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "齐料日期")
    private Date itemReadyDate;
    /**
     * 来源
     * @see ProductionOrderSourceEnum#getCode()
     * */
    @TableField("SOURCE")
    @ApiModelProperty(value = "来源")
    private String source;
    /**
     * 来源
     * @see ProductionOrderSourceEnum#getDesc()
     * */
    @TableField(exist = false)
    @ApiModelProperty(value = "来源说明")
    private String sourceDesc;
    /**
     * 备注
     * */
    @TableField("REMARK")
    @ApiModelProperty(value = "备注")
    private String remark;
    /**
     * 创建人id
     * */
    @TableField("CREATE_BY")
    @ApiModelProperty(value = "创建人id", hidden = true)
    private String createBy;
    /**
     * 创建时间
     * */
    @TableField("CREATE_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private Date createDate;
    /**
     * 更新人
     * */
    @TableField("UPDATE_BY")
    @ApiModelProperty(value = "更新人", hidden = true)
    private String updateBy;
    /**
     * 更新时间
     * */
    @TableField("UPDATE_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATE_PATTERN, timezone = "GMT+8")
    @ApiModelProperty(value = "更新时间", hidden = true)
    private Date updateDate;


}
