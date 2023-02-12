package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("iap_sys_param")
public class IapSysParam extends BaseEntity {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    /**
     * 参数类型id
     */
    @TableField("iap_sys_param_type_id")
    @ApiModelProperty(value = "参数类型id")
    private String iapSysParamTypeId;

    /**
     * 参数组名称
     */
    @TableField("name")
    @ApiModelProperty(value = "参数组名称")
    private String name;

    /**
     * 参数组描述
     */
    @TableField("remark")
    @ApiModelProperty(value = "参数组描述")
    private String remark;

    /**
     * 启用状态（0：启用，1：禁用）
     */
    @TableField("state")
    @ApiModelProperty(value = "启用状态（0：启用，1：禁用）")
    private Integer state;

    @TableField(exist = false)
    private String stateName;

    /**
     * 工厂id
     */
    @TableField("site_id")
    @ApiModelProperty(value = "工厂id")
    private String siteId;

    @TableField("createDate")
    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @TableField("createBy")
    @ApiModelProperty(value = "创建人")
    private String createUser;

    @TableField("updateDate")
    @ApiModelProperty(value = "修改时间")
    private Date updateDate;

    @TableField("updateBy")
    @ApiModelProperty(value = "修改人")
    private String updateUser;

}
