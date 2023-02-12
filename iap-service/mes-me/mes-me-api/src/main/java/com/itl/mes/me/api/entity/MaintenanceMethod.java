package com.itl.mes.me.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 维修方法
 * @author dengou
 * @date 2021/11/4
 */
@Data
@TableName("me_maintenance_method")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MaintenanceMethod {

    /**
     * 主键id
     * */
    @TableId(value = "id", type = IdType.UUID)
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 工厂id
     * */
    @TableField(value = "site")
    @ApiModelProperty(value = "工厂id", hidden = true)
    private String site;
    /**
     * 维修措施编码
     * */
    @TableField("code")
    @ApiModelProperty("维修措施编码")
    @NotNull(message = "维修措施编码不能为空")
    private String code;
    /**
     * 维修措施标题/名称
     * */
    @TableField("title")
    @ApiModelProperty("维修措施标题/名称")
    @NotNull(message = "维修措施名称不能为空")
    private String title;
    /**
     * 维修措施说明
     * */
    @TableField("description")
    @ApiModelProperty("维修措施说明")
    private String description;
    /**
     * 异常类型id
     * */
    @TableField("error_type_id")
    @ApiModelProperty("异常类型id")
    private String errorTypeId;
    /**
     * 异常类型完整id路径， 从一级父节点到叶子节点，用'-'分隔
     * */
    @TableField("error_type_full_ids")
    @ApiModelProperty("异常类型完整id路径， 从一级父节点到叶子节点，用'-'分隔")
    private String errorTypeFullIds;
    /**
     * 维修方法
     * */
    @TableField("method")
    @ApiModelProperty("维修方法")
    private String method;
    /**
     * 维修位置
     * */
    @TableField("place")
    @ApiModelProperty("维修位置")
    private String place;
    /**
     * 备注
     * */
    @TableField("remark")
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 禁用flag, Y/N
     * */
    @TableField("is_disable_flag")
    @ApiModelProperty("禁用flag")
    private String isDisableFlag;
    /**
     * 创建人用户名
     * */
    @TableField("create_user")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String createUser;
    /**
     * 创建时间
     * */
    @TableField("create_time")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date createTime;
    /**
     * 更新人用户名
     * */
    @TableField("update_user")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private String updateUser;
    /**
     * 更新时间
     * */
    @TableField("update_time")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    @JsonIgnore
    @ApiModelProperty(hidden = true)
    private Date updateTime;


}
