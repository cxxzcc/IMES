package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 包装
 *
 * @author cch
 * @date 2021-06-16
 */
@Data
@TableName("me_pack")
@ApiModel(value = "me_pack", description = "Sfc包装")
public class Pack implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "BO", type = IdType.INPUT)
	@ApiModelProperty(value = "唯一标识【PK】")
	private String bo;

	@TableField("SITE")
	@ApiModelProperty(value = "工厂")
	private String site;

	@TableField("SN")
	@ApiModelProperty(value = "条码")
	private String sn;

	@TableField("PACK_QTY")
	@ApiModelProperty(value = "包装数量")
	private Integer packQty;

	@TableField("OPERATION_BO")
	@ApiModelProperty(value = "工序Bo")
	private String operationBo;

	@TableField("SHOP_ORDER_BO")
	@ApiModelProperty(value = "工单Bo")
	private String shopOrderBo;

	@TableField("SCHEDULE_NO")
	@ApiModelProperty(value = "排程号")
	private String scheduleNo;

	@TableField("ITEM_BO")
	@ApiModelProperty(value = "物料Bo")
	private String itemBo;

	@TableField("ITEM_NAME")
	@ApiModelProperty(value = "物料名称")
	private String itemName;

	@TableField("PACK_LEVEL")
	@ApiModelProperty(value = "包装层级")
	private String packLevel;

	@TableField("OUTER_PACK_SN")
	@ApiModelProperty(value = "外层包装条码")
	private String outerPackSn;

	@TableField("PACK_DATE")
	@ApiModelProperty("装箱时间")
	private Date packDate;
}
