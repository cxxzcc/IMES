package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * SN-包装规则
 *
 * @author cch
 * @date 2021-06-16
 */
@Data
@TableName("me_pack_rule_temp")
@ApiModel(value = "me_pack_rule_temp", description = "${comments}")
public class PackRuleTemp implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId(value = "BO", type = IdType.INPUT)
	@ApiModelProperty(value = "主键")
	private String bo;

	@TableField("OPERATION_BO")
	@ApiModelProperty(value = "当前工序Bo")
	private String operationBo;

	@TableField("SN")
	@ApiModelProperty(value = "条码")
	private String sn;

	@TableField("PACK_LEVEL")
	@ApiModelProperty(value = "包装层级")
	private String packLevel;

	@TableField("PACK_NAME")
	@ApiModelProperty(value = "包装名称")
	private String packName;

	@TableField("MIN_QTY")
	@ApiModelProperty(value = "最小包装数")
	private Integer minQty;

	@TableField("MAX_QTY")
	@ApiModelProperty(value = "最大包装数")
	private Integer maxQty;

	@TableField("RULE_LABEL_BO")
	@ApiModelProperty(value = "包装规则模板Bo")
	private String ruleLabelBo;

	@TableField(exist = false)
	@ApiModelProperty(value = "规则模板名称")
	private String ruleLabelName;

}
