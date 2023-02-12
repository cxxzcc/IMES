package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 过站动作
 *
 * @author cch
 * @date 2021-05-31
 */
@Data
@TableName("me_action")
@ApiModel(value = "me_action", description = "过站动作")
public class Action implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableField("action")
    @ApiModelProperty(value = "动作")
    private String action;

    @TableField("action_desc")
    @ApiModelProperty(value = "描述")
    private String actionDesc;

    @TableField("is_default")
    @ApiModelProperty(value = "是否默认")
    private String isDefault;

    @TableField("state")
    @ApiModelProperty(value = "状态")
    private String state;

    @TableField("create_date")
    private Date createDate;

    @TableField("id")
    private String id;
    @TableField("modify_date")
    private Date modifyDate;
    @TableField("create_user")
    private String createUser;

    @TableField("site")
    private String site;

    @TableField("modify_user")
    private String modifyUser;
	@TableField(exist = false)
    private List<ActionOperation> list;

}
