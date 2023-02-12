package com.itl.mes.me.provider.enums;

/**
 * @auth liuchenghao
 * @date 2021/3/29
 */
public enum LabelTemplateTypeEnum {

    /**
     * 标签类型枚举，目前啊有两种标签类型，一种是jasper需后端生成pdf文件给前端打印，
     * 一种是lodop，根据lodop生成的代码，前端自行打印
     */
    JASPER("JASPER","JASPER"),
    LODOP("LODOP","LODOP");

    private String code;

    private String value;


    LabelTemplateTypeEnum(String code, String value) {
        this.code =code;
        this.value = value;
    }

    public String getCode(){
        return code;
    }

    public String getValue(){
        return value;
    }

}
