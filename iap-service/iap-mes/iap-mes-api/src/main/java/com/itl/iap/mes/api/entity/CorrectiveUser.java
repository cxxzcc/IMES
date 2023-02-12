package com.itl.iap.mes.api.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yx
 * @date 2021/8/16
 * @since 1.8
 */
@Data
@ApiModel("设备维修人表")
@TableName("m_repair_corrective_user")
public class CorrectiveUser implements Serializable {
    /**
     * 工单id
     */
    @TableField("REPAIR_ID")
    private String repairId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField("REPAIR_USER_ID")
    private String repairUserId;

    /**
     * 用户账号
     */
    @ApiModelProperty("用户账号")
    @TableField(exist = false)
    private String repairUserAcount;

    /**
     * 用户名
     */
    @ApiModelProperty("用户名")
    @TableField("REPAIR_USER_NAME")
    private String repairUserName;

    /**
     * 指派时间
     */
    @ApiModelProperty("指派时间")
    @TableField("ASSIGN_DATE")
    @JsonFormat(pattern = DatePattern.NORM_DATETIME_PATTERN, timezone = "GMT+8")
    private Date assignDate;
}
