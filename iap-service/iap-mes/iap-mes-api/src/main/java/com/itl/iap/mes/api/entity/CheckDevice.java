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
 * @date 2021/8/26
 * @since 1.8
 */
@Data
@TableName("m_check_device")
@ApiModel("点检计划-设备-中间表")
public class CheckDevice {

    @TableId(type = IdType.UUID)
    @TableField("ID")
    private String id;

    @TableField("CHECK_ID")
    @ApiModelProperty("点检计划id")
    private String checkId;

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
