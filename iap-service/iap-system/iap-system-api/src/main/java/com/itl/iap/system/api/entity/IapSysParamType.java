package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_param_type")
public class IapSysParamType {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 工厂代码
     */
    @TableField("site")
    @ApiModelProperty(value = "工厂代码")
    private String site;

    /**
     * 参数类型
     */
    @TableField("type")
    @ApiModelProperty(value = "参数类型")
    private String type;

    /**
     * 类型描述
     */
    @TableField("remark")
    @ApiModelProperty(value = "类型描述")
    private String remark;

    /**
     * 启用状态（0：启用，1：禁用）
     */
    @TableField("state")
    @ApiModelProperty(value = "启用状态（0：启用，1：禁用）")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    @TableField("create_date")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @TableField("create_user")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("update_date")
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    @TableField("update_user")
    @ApiModelProperty(value = "修改人")
    private String updateUser;
}
