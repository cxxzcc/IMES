package com.itl.mes.core.api.constant;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 生产订单状态枚举
 * @author dengou
 * @date 2021/10/11
 */
@Getter
public enum ProductionOrderStatusEnum {

    /**
     * 未下达
     * */
    NOT_ASSIGN("1", "未下达"),
    /**
     *已下达
     * */
    ASSIGNED("2", "已下达"),
    /**
     *生产中
     * */
    IN_PRODUCTION("3", "生产中"),
    /**
     *完工
     * */
    COMPLETE("4", "完工"),
    /**
     *关闭
     * */
    CLOSED("5", "关闭"),


    ;
    private String code;
    private String desc;

    ProductionOrderStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }
    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static ProductionOrderStatusEnum parseByCode(String code) {
        return Arrays.stream(ProductionOrderStatusEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<ProductionOrderStatusEnum> first = Arrays.stream(ProductionOrderStatusEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
