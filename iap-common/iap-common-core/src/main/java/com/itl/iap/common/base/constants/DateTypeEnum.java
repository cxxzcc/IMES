package com.itl.iap.common.base.constants;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 时间范围
 */
@Getter
@AllArgsConstructor
public enum DateTypeEnum {
    /**
     * 成功
     */
    MONTH("0", "月"),

    /**
     * liKun
     * 返回业务提示及数据
     */
    DAY("1", "天"),


    /**
     * 第三方服务异常
     */
    HOUR("2", "时");


    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;
}
