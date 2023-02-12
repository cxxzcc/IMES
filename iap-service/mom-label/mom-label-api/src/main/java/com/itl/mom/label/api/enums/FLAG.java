package com.itl.mom.label.api.enums;

import com.itl.mes.core.api.constant.CustomDataTypeEnum;

/**
 * @author yx
 * @date 2021/3/17
 * @since JDK1.8
 */
public enum FLAG {
    ITEM("m_item", CustomDataTypeEnum.ITEM.getDataType()),
    SHOP_ORDER("m_shop_order", CustomDataTypeEnum.SHOP_ORDER.getDataType()),
    CARRIER("m_carrier", CustomDataTypeEnum.CARRIER.getDataType()),
    DEVICE("m_device", CustomDataTypeEnum.DEVICE.getDataType()),
    DEVICE_TYPE("m_device_type", ""),
    PACK("p_packing", CustomDataTypeEnum.PACKING.getDataType()),
    PACK_LEVEL("p_pack_level", "");
    private String tableName;
    private String customDataType;

    FLAG(String tableName, String customDataType) {
        this.tableName = tableName;
        this.customDataType = customDataType;
    }

    public String getTableName() {
        return tableName;
    }

    public String getCustomDataType() {
        return customDataType;
    }
}
