package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/17
 * @since 1.8
 */
@TableName("m_repair_check_user")
@Data
@ApiModel("点检人关联")
public class CheckPlanUser {

    @TableId(type = IdType.UUID)
    @TableField("ID")
    private String id;

    @ApiModelProperty("点检计划id")
    @TableField("CHECK_ID")
    private String checkId;

    @ApiModelProperty("点检人编码")
    @TableField("CHECK_USER_ID")
    private String checkUserId;

    @ApiModelProperty("点检人名称")
    @TableField(exist = false)
    private String checkUserName;

    @ApiModelProperty("0 操作员 1 其他")
    @TableField("IDENTITY_TYPE")
    private String identityType;
}
