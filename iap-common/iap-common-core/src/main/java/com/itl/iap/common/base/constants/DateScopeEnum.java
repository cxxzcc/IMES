package com.itl.iap.common.base.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间范围
 */
@Getter
@AllArgsConstructor
public enum DateScopeEnum {
    /**
     * 成功
     */
    TODAY("TODAY", "今日"),

    /**
     * liKun
     * 返回业务提示及数据
     */
    YESTERDAY("YESTERDAY", "昨日"),

    /**
     * 默认错误
     */
    WEEK("WEEK", "本周"),

    /**
     * 第三方服务异常
     */
    MONTH("MONTH", "本月");


    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;
}
