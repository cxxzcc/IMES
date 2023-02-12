package com.itl.iap.mes.api.common;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 工单类型枚举
 * @author dengou
 * @date 2021/10/25
 */
@Getter
public enum OrderTypeEnum {

    /**维修*/
    CORRECTIVE("corrective", "维修"),
    /**保养*/
    UPKEEP("upkeep", "保养"),
    /**点检*/
    CHECK("check", "点检"),
    ;


    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    OrderTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static OrderTypeEnum parseByCode(String code) {
        return Arrays.stream(OrderTypeEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<OrderTypeEnum> first = Arrays.stream(OrderTypeEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
