package com.itl.mes.core.api.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author yx
 * @date 2021/8/24
 * @since 1.8
 */
@TableName("m_device_type_fault")
@Data
public class DeviceTypeFault {
    @TableField("DEVICE_TYPE_BO")
    private String deviceTypeBo;
    @TableField("FAULT_ID")
    private String faultId;
}
