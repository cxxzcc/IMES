package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.itl.iap.common.base.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

@Data
@TableName("t_project")
@ApiModel("项目实体")
public class TProject extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("项目编码")
    @TableField("code")
    private String code;

    @ApiModelProperty("项目名称")
    @TableField("name")
    private String name;

    @ApiModelProperty("仪器类型id")
    @TableField("instrumentTypeId")
    private String instrumentTypeId;

    @ApiModelProperty("标准偏差")
    @TableField("standardDeviation")
    private String standardDeviation;

    @ApiModelProperty("单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty("小数位")
    @TableField("digital")
    private Integer digital;

    @ApiModelProperty("仪器类型名称")
    @TableField(exist = false)
    private String instrumentTypeName;



}
