package com.itl.mes.core.api.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 生产订单来源
 * @author dengou
 * @date 2021/10/11
 */
@Getter
public enum ProductionOrderSourceEnum {

    /**
     * ERP
     * */
    ERP("ERP", "ERP/外部"),
    /**
     *MES
     * */
    MES("MES", "MES自建"),

    ;
    private String code;
    private String desc;

    ProductionOrderSourceEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static ProductionOrderSourceEnum parseByCode(String code) {
        return Arrays.stream(ProductionOrderSourceEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<ProductionOrderSourceEnum> first = Arrays.stream(ProductionOrderSourceEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
