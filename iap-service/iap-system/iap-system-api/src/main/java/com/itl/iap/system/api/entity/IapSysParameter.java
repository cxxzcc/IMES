package com.itl.iap.system.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("iap_sys_parameter")
public class IapSysParameter extends BaseEntity implements Serializable {
    private static final long serialVersionUID = -30729856515700265L;

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty(value = "参数编码")
    @TableField("code")
    private String code;

    @ApiModelProperty(value = "参数名称")
    @TableField("name")
    private String name;

    @ApiModelProperty(value = "字典项id")
    @TableField("dictItemId")
    private String dictItemId;

    @ApiModelProperty(value = "字典项名称")
    @TableField(exist = false)
    private String dictItemName;

    @ApiModelProperty(value = "显示名称")
    @TableField("showValue")
    private String showValue;

    @ApiModelProperty(value = "默认值")
    @TableField("defaultValue")
    private String defaultValue;

    @ApiModelProperty(value = "描述")
    @TableField("remark")
    private String remark;


}
