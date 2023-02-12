package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * ${comments}
 *
 * @author cch
 * @date 2021-05-31
 */
@Data
@TableName("me_action_operation")
@ApiModel(value = "me_action_operation", description = "${comments}")
@Accessors(chain = true)
public class ActionOperation implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField("ao_order")
	@ApiModelProperty(value = "顺序")
	private String order;

	@TableField("ao_desc")
	@ApiModelProperty(value = "描述")
	private String desc;

	@TableField("Data_collection_group_id")
	@ApiModelProperty(value = "数据收集组")
	private String dataCollectionGroupId;

	@TableField("action_id")
	private String actionId;

	@TableField("operation_id")
	private String operationId;

}
