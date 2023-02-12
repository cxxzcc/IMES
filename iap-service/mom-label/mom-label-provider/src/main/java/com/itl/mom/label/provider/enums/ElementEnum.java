package com.itl.mom.label.provider.enums;

/**
 * @auth liuchenghao
 * @date 2021/3/30
 */
public enum ElementEnum {

    /**
     * item 代表物料，carrier代表载具，shop_order代表工单，packing代表包装，device代表设备
     */
    ITEM("ITEM", "M_ITEM", "ITEM"),
    CARRIER("CARRIER", "M_CARRIER", "CARRIER"),
    SHOP_ORDER("SHOP_ORDER", "M_SHOP_ORDER", "SHOP_ORDER"),
    PACKING("PACKING", "P_PACKING", "PACK_NAME"),
    DEVICE("DEVICE", "M_DEVICE", "DEVICE");

    /**
     * 元素的类型
     */
    private String type;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 查询的字段
     */
    private String queryColumn;

    ElementEnum(String type, String tableName, String queryColumn) {
        this.type = type;
        this.tableName = tableName;
        this.queryColumn = queryColumn;
    }

    public String getQueryColumn() {
        return queryColumn;
    }

    public String getTableName() {
        return tableName;
    }

    public String getType() {
        return type;
    }
}
