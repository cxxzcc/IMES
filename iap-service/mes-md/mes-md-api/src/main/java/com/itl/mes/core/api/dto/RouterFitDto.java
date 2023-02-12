package com.itl.mes.core.api.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 工艺路线设置表DTO
 *
 * @author xtz
 * @date 2021-05-25
 */
@Data
public class RouterFitDto{

	@ApiModelProperty(value = "分页信息")
	private Page page;

	@ApiModelProperty(value = "主键BO")
	private String bo;

	@ApiModelProperty(value = "当前工厂")
	private String site;

	@ApiModelProperty(value = "工单类型")
	private String shopOrderType;

	@ApiModelProperty(value = "元素BO")
	private String elementBo;

	@ApiModelProperty(value = "元素")
	private String element;

	@ApiModelProperty(value = "元素名称")
	private String elementName;

	@ApiModelProperty(value = "工艺路线BO")
	private String routerBo;

	@ApiModelProperty(value = "工艺路线")
	private String router;

	@ApiModelProperty(value = "工艺路线版本")
	private String routerVersion;

	@ApiModelProperty(value = "物料清单BO")
	private String bomBo;

	@ApiModelProperty(value = "物料清单")
	private String bom;

	@ApiModelProperty(value = "物料清单版本")
	private String bomVersion;



}
