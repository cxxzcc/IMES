package com.itl.mes.core.api.constant;

/**
 * 工单维护状态枚举
 * @author cjq
 * @date 2021/9/10
 * @since JDK1.8
 */
public enum OrderStateEnum {
    NEW("500", "新建"),
    PART_ORDER("501", "部分下达"),
    ORDER("502", "已下达"),
    ING("503", "生产中"),
    OVER("504", "完工"),
    CLOSED("505", "关闭"),
    PAUSE("506", "暂停"),
    PRO_PAUSE("507", "生产中暂停");

    private String code;
    private String desc;

    OrderStateEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }
}
