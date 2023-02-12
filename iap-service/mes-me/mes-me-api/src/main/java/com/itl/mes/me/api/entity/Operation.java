package com.itl.mes.me.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 操作
 *
 * @author yx
 * @date 2021-05-31
 */
@Data
@TableName("me_operation")
@ApiModel(value = "me_operation", description = "操作")
public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.INPUT)
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "工厂")
    @TableField("SITE")
    private String site;
    @ApiModelProperty(value = "操作")
    @TableField("OPERATION")
    private String operation;
    @ApiModelProperty(value = "描述")
    @TableField("[DESC]")
    private String desc;

    @ApiModelProperty("扫描条码")
    @TableField("SCAN_SN")
    private String scanSn;

    @TableField(exist = false)
    private String scanSnName;

    @TableField(exist = false)
    private String scanSnJson;

    @ApiModelProperty(value = "code")
    @TableField("CODE")
    private String code;

    @ApiModelProperty(value = "建档日期")
    @TableField("CREATE_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date createDate;

    @ApiModelProperty(value = "建档人")
    @TableField("CREATE_USER")
    private String createUser;

    @ApiModelProperty(value = "修改日期")
    @TableField("MODIFY_DATE")
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss.SSS",
            timezone = "GMT+8"
    )
    private Date modifyDate;

    @ApiModelProperty(value = "修改人")
    @TableField("MODIFY_USER")
    private String modifyUser;

    public void setObjectSetBasicAttribute(String userId, Date date) {
        this.createUser = userId;
        this.createDate = date;
        this.modifyUser = userId;
        this.modifyDate = date;
    }
}
