package com.itl.mes.core.api.constant;

import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 暂存数据类型
 * @author dengou
 * @date 2021/11/17
 */
@Getter
@AllArgsConstructor
public enum TemporaryDataTypeEnum {

    /**
     * 定性检验
     * */
    QUALITATIVE("qualitative", "定性检验"),
    /**
     * 定量检验
     * */
    QUANTIFY("quantify", "定量检验"),
    /**
     * 维修
     * */
    REPAIR("repair", "维修"),

    /**
     * 包装
     * */
    PACK("pack", "包装"),
    /**
     * 单体条码绑定
     */
    BARCODEBIND("barcodeBind","单体条码绑定"),
    /**
     * 批次条码绑定
     */
    BATCHBARCODE("batchBarcode","批次条码绑定"),
    /**
     * 在线打印
     */
    IN_PRODUCT_LINE_PRINT("inProductLinePrint","在线打印"),

    ;


    private String code;
    private String desc;

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static TemporaryDataTypeEnum parseByCode(String code) {
        return Arrays.stream(TemporaryDataTypeEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<TemporaryDataTypeEnum> first = Arrays.stream(TemporaryDataTypeEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
