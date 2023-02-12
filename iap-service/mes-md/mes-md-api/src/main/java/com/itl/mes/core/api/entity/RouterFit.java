package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.mes.core.api.vo.CustomDataAndValVo;
import com.itl.mes.core.api.vo.CustomDataValVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 工艺路线设置表
 *
 * @author xtz
 * @date 2021-05-25
 */
@Data
@TableName("m_router_fit")
@ApiModel(value = "m_router_fit", description = "工艺路线设置表")
public class RouterFit implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableId("BO")
	@ApiModelProperty(value = "主键BO")
	private String bo;

	@TableField("SHOP_ORDER_TYPE")
	@ApiModelProperty(value = "工单类型")
	private String shopOrderType;

	@TableField("ELEMENT_BO")
	@ApiModelProperty(value = "元素BO")
	private String elementBo;

	@TableField("ROUTER_BO")
	@ApiModelProperty(value = "工艺路线BO")
	private String routerBo;

	@TableField("BOM_BO")
	@ApiModelProperty(value = "物料清单BO")
	private String bomBo;

	@TableField("SITE")
	@ApiModelProperty(value = "当前工厂")
	private String site;

	@TableField(exist = false)
	private List<CustomDataAndValVo> customDataAndValVoList;
	@TableField(exist = false)
	private List<CustomDataValVo> customDataValVoList;

}
