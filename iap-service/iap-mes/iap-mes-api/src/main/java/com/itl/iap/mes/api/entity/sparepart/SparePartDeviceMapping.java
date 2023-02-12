package com.itl.iap.mes.api.entity.sparepart;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;

/**
 * 设备-备件关联表
 * @author dengou
 * @date 2021/9/17
 */
@Data
@TableName("m_spare_part_device_mapping")
public class SparePartDeviceMapping {

    @TableId(value = "ID", type = IdType.UUID)
    private String id;

    /**
     * 备件id
     * */
    @TableField("SPARE_PART_ID")
    private String sparePartId;

    /**
     * 设备id
     * */
    @TableField("DEVICE_ID")
    private String deviceId;

    public SparePartDeviceMapping() {
    }

    @Builder
    public SparePartDeviceMapping(String id, String sparePartId, String deviceId) {
        this.id = id;
        this.sparePartId = sparePartId;
        this.deviceId = deviceId;
    }
}
