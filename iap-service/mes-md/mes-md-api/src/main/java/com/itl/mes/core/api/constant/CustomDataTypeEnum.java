package com.itl.mes.core.api.constant;

/**
 * 自定义类型数据常量值，对应表名去掉表名前缀M_、P_等
 *
 */
public enum CustomDataTypeEnum {

    ITEM( "ITEM","ITEM" ),
    ITEM_GROUP( "ITEM_GROUP","ITEM_GROUP" ),
    OPERATION( "OPERATION","OPERATION" ),
    SHOP_ORDER( "SHOP_ORDER","SHOP_ORDER" ),
    PRODUCT_LINE( "PRODUCT_LINE","PRODUCT_LINE" ),
    WORK_SHOP( "WORK_SHOP","WORK_SHOP" ),
    DC_GROUP( "DC_GROUP","DC_GROUP" ),
    CUSTOMER( "CUSTOMER","CUSTOMER" ),
    CUSTOMER_ORDER( "CUSTOMER_ORDER","CUSTOMER_ORDER" ),
    DATA_LIST( "DATA_LIST","DATA_LIST" ),
    BOM( "BOM","BOM" ),
    VENDOR("VENDOR","VENDOR"),
    NC_CODE("NC_CODE","NC_CODE"),
    DEVICE("DEVICE","DEVICE"),
    INSTRUCTOR("INSTRUCTOR","INSTRUCTOR"),
    TEAM("TEAM","TEAM"),
    UOM_ITEM("UOM_ITEM", "UOM_ITEM"),
    UOM("UOM", "UOM"),
    EVENT_REQUEST("EVENT_REQUEST","EVENT_REQUEST"),
    TRANSACTION_TYPE("TRANSACTION_TYPE","TRANSACTION_TYPE"),
    EVENT_TYPE("EVENT_TYPE" , "EVENT_TYPE"),
    ROUTER("ROUTER","ROUTER"),
    CARRIER("CARRIER","CARRIER"),
    PACKING("PACKING","PACKING"),
    STATION( "STATION","STATION" ),
    INSPECT_TYPE("INSPECT_TYPE","INSPECT_TYPE"),
    QUALITY_PLAN("QUALITY_PLAN","QUALITY_PLAN"),
    ORGANIZATION_USER("ORGANIZATION_USER","ORGANIZATION_USER"),
    WAREHOUSE("WAREHOUSE","WAREHOUSE"),
    LABEL("LABEL", "LABEL"),
    SUPPLIER("SUPPLIER", "SUPPLIER"),
    PORTAL_CUSTOMER("PORTAL_CUSTOMER", "PORTAL_CUSTOMER"),
    WMS_WAREHOUSE("WMS_WAREHOUSE","WMS_WAREHOUSE"),
    WMS_AREA("WMS_AREA","WMS_AREA"),
    ROUTER_FIT("ROUTER_FIT","ROUTER_FIT"),
    /**备件*/
    SPARE_PART("SPARE_PART","SPARE_PART"),
    ;

    private String dataType;
    private String description;


    CustomDataTypeEnum(String dataType ){
        this.dataType= dataType;
    }

    CustomDataTypeEnum(String dataType, String description ){
        this.dataType= dataType;
        this.description = description;
    }

    public String getDataType() {
        return dataType;
    }

    public String getDescription() {
        return description;
    }

}
