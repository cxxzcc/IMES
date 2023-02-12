package com.itl.mom.label.api.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 标签打印日志类型枚举
 * @author dengou
 * @date 2021/12/1
 */
@Getter
@AllArgsConstructor
public enum LabelPrintLogStateEnum {

    /**在线打印*/
    ON_PRODUCT_LINE("1", "在线打印"),
    /**补打*/
    ADDITIONAL("2", "补打"),
    /**报废*/
    SCRAPPED("3", "报废"),
    /**新建*/
    NEW("4", "新建"),


    ;

    private String code;
    private String desc;

}
