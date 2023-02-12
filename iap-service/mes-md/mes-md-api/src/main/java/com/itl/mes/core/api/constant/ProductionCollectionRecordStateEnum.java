package com.itl.mes.core.api.constant;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 生产采集记录状态枚举
 * @author dengou
 * @date 2021/11/11
 */
@Getter
@AllArgsConstructor
public enum ProductionCollectionRecordStateEnum {

    complete(1, "完成"),

    ;
    private Integer code;

    private String desc;

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static ProductionCollectionRecordStateEnum parseByCode(Integer code) {
        return Arrays.stream(ProductionCollectionRecordStateEnum.values())
                .filter(e-> StrUtil.equals(e.getCode()+"", code+"")).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(Integer code) {
        Optional<ProductionCollectionRecordStateEnum> first = Arrays.stream(ProductionCollectionRecordStateEnum.values())
                .filter(e -> StrUtil.equals(e.getCode()+"", code+"")).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
