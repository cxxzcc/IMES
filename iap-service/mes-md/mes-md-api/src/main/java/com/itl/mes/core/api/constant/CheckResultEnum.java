package com.itl.mes.core.api.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CheckResultEnum {

    /**
     * ok
     * */
    OK("OK", "OK"),
    /**
     * ng
     * */
    NG("NG", "NG"),

    ;

    private final String code;
    private final String desc;

}
