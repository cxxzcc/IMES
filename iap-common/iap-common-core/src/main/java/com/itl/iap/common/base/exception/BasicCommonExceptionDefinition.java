package com.itl.iap.common.base.exception;

import lombok.Getter;

/**
 * commomException 基础异常code枚举
 * @author dengou
 * @date 2021/10/19
 */
@Getter
public enum BasicCommonExceptionDefinition {

    /**
     * 没有cookie
     * */
    NON_COOKIE(10010, "没有获取到cookie"),
    /**
     * 工厂时长到期
     * */
    SITE_EXPIRY(10020, "工厂有限时长已过期，请切换工厂！"),
    /**
     * 工位时长到期
     * */
    STATION_EXPIRY(10021, "工位有限时长已过期，请切换工位！"),
    /**
     * 车间时长到期
     * */
    WORK_SHOP_EXPIRY(10022, "车间有限时长已过期，请切换车间！"),
    /**
     * 产线时长到期
     * */
    PRODUCT_LINE_EXPIRY(10023, "产线有限时长已过期，请切换产线！"),

    ;

    private Integer code;
    private String msg;

    BasicCommonExceptionDefinition(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
