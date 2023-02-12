package com.itl.mes.me.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.itl.iap.common.base.model.TreeNode;
import com.itl.iap.common.util.group.ValidationGroupUpdate;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 异常类型
 * @author dengou
 * @date 2021/11/3
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("me_error_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorType extends TreeNode<ErrorType> {

    /**
     * id
     * */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty(value = "id")
    @NotNull(groups = {ValidationGroupUpdate.class}, message = "id不能为空")
    private String id;

    /**
     * 工厂id
     * */
    @TableField("SITE")
    @ApiModelProperty(value = "工厂id", hidden = true)
    private String site;

    /**
     * 异常代码
     * */
    @TableField("ERROR_CODE")
    @ApiModelProperty(value = "异常代码")
    @NotNull(message = "异常代码不能为空")
    private String errorCode;
    /**
     * 异常名称
     * */
    @TableField("ERROR_NAME")
    @ApiModelProperty(value = "异常名称")
    @NotNull(message = "异常名称不能为空")
    private String errorName;
    /**
     * 异常描述
     * */
    @TableField("ERROR_DESC")
    @ApiModelProperty(value = "异常描述")
    private String errorDesc;
    /**
     * 父节点id， 顶级节点的父节点id为null
     * */
    @TableField("PARENT_ID")
    @ApiModelProperty(value = "父节点id")
    private String parentId;

    /**
     * 启用 Y/禁用 N flag
     * */
    @TableField("IS_DISABLE_FLAG")
    @ApiModelProperty(value = "启用 Y/禁用 N flag")
    private String isDisableFlag;


    @TableField("CREATE_USER")
    @JsonIgnore
    @ApiModelProperty(hidden = true, value = "创建人用户名")
    private String createUser;

    @TableField("CREATE_TIME")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JsonIgnore
    @ApiModelProperty(hidden = true, value = "创建时间")
    private Date createTime;

    @TableField("UPDATE_USER")
    @JsonIgnore
    @ApiModelProperty(hidden = true, value = "更新人用户名")
    private String updateUser;

    @TableField("UPDATE_TIME")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JsonIgnore
    @ApiModelProperty(hidden = true, value = "更新时间")
    private Date updateTime;
}
