package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.FieldNameConstants;

@Data
@FieldNameConstants
@TableName("t_base")
@ApiModel("基地实体")
public class TBase extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("基地编码")
    @TableField("code")
    private String code;

    @ApiModelProperty("基地名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("是否启用")
    @TableField("isUse")
    private String isUse;

    @ApiModelProperty("基地描述")
    @TableField("remark")
    private String remark;




}
