package com.itl.iap.mes.api.common;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 点检工单状态
 */
@Getter
@AllArgsConstructor
public enum CheckStateEnum {

    DDZ(0, "待点检"),
    WC(1, "完成"),
    DJZ(2, "点检中"),
    YGQ(3, "已过期"),
    YC(4, "异常");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;


}
