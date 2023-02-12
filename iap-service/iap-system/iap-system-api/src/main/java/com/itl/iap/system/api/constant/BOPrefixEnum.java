package com.itl.iap.system.api.constant;

public enum BOPrefixEnum {
    /**
     * 用户自定义列配置表
     */
    USER_CUSTOM_CONFIG("UCC", "USER_CUSTOM_COLUMN_CONFIG");

    private String prefix;
    private String table;

    BOPrefixEnum(String prefix, String table ){
        this.prefix = prefix;
        this.table = table;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getTable() {
        return table;
    }
}
