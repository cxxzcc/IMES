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
@TableName("m_repair_upkeep_user")
@Data
@ApiModel("设备保养人")
public class UpkeepUser {

    @TableId(type = IdType.UUID)
    @TableField("ID")
    private String id;

    @ApiModelProperty("设备保养id")
    @TableField("UPKEEP_ID")
    private String upkeepId;

    @ApiModelProperty("保养人id")
    @TableField("UPKEEP_USER_ID")
    private String upkeepUserId;

    @ApiModelProperty(" 保养人姓名")
    @TableField(exist = false)
    private String upkeepUserName;

    @ApiModelProperty("操作角色 0：操作员 1其他")
    @TableField("IDENTITY_TYPE")
    private String identity_type;
}
