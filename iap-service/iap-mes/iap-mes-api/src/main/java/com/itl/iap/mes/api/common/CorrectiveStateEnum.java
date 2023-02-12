package com.itl.iap.mes.api.common;


import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 维修工单状态
 */
@Getter
@AllArgsConstructor
public enum CorrectiveStateEnum {

    DWX(0, "待维修"),
    WXZ(1, "维修中"),
    WC(2, "完成"),
    ;


    /**
     * 状态码
     */
    private Integer code;

    /**
     * 描述
     */
    private String desc;



}
