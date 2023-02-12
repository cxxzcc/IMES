package com.itl.iap.mes.api.common;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 保养工单状态
 */
@Getter
@AllArgsConstructor
public enum UpkeepStateEnum {

    DBY(0, "待保养"),
    WC(1, "完成"),
    BYZ(2, "保养中"),
    YGQ(3, "已过期");


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;



}
