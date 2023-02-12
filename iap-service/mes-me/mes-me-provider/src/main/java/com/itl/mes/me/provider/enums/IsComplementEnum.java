package com.itl.mes.me.provider.enums;

/**
 * @auth liuchenghao
 * @date 2021/3/26
 * 是否补码枚举
 */
public enum IsComplementEnum {

    /**
     * 是否补码的枚举，Y代表是，N代表否
     */
    Y("COMPLEMENT",1),
    N("NO_COMPLEMENT",2);

    private String code;
    
    private Integer value;

    IsComplementEnum(String code, int value) {
        this.code =code;
        this.value = value;
    }

    public String getCode(){
        return code;
    }

    public Integer getValue(){
        return value;
    }

}
