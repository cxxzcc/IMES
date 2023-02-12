package com.itl.mes.core.api.constant;

public enum RouterProcessEnum {
    START("START", "开始节点"),
    END("END", "结束节点");

    private String code;
    private String desc;

    RouterProcessEnum(String code, String desc) {
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
