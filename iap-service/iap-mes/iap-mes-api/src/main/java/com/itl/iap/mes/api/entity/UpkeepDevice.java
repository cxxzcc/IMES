package com.itl.iap.mes.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/26
 * @since 1.8
 */
@TableName("m_upkeep_device")
@Data
@ApiModel("保养计划-设备-中间表")
public class UpkeepDevice {

    @TableId(type = IdType.UUID)
    @TableField("ID")
    private String id;

    @TableField("UPKEEP_ID")
    @ApiModelProperty("关联字段")
    private String upkeepId;

    @TableField("DEVICE_ID")
    @ApiModelProperty("设备id")
    private String deviceId;

    @TableField(exist = false)
    @ApiModelProperty("设备编号")
    private String code;

    @TableField(exist = false)
    @ApiModelProperty("设备名称")
    private String name;

    @TableField(exist = false)
    @ApiModelProperty("设备类型")
    private String type;
}
