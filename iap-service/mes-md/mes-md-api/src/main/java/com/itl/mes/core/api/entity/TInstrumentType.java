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
@TableName("t_instrument_type")
@ApiModel("仪器类型")
@FieldNameConstants
public class TInstrumentType extends BaseEntity {

    @TableId(type = IdType.UUID)
    private String id;

    @ApiModelProperty("项目名称")
    @TableField("name")
    private String name;


}
