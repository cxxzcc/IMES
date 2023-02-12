package com.itl.mes.core.api.constant;

// 工单 类型 枚举
public enum ShopOrderTypeEnum {
    // 工单类型 试产trial  量产normal  返工rework

    TRIAL("trial", "试产"),
    NORMAL("normal", "量产"),
    REWORK("rework", "返工");

    private final String code;
    private final String desc;

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    ShopOrderTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
