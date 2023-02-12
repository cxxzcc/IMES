package com.itl.iap.system.api.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AppSystemTypeEnum {
    /**
     * 安卓
     */
    ANDROID("1101", "安卓"),

    /**
     * 苹果
     * 返回业务提示及数据
     */
    IOS("1102", "ios");


    /**
     * 编码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;
}
