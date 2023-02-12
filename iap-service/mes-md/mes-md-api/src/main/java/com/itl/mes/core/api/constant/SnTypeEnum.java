package com.itl.mes.core.api.constant;

import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * @author cch
 * @date 2021/4/28$
 * @since JDK1.8
 */
public enum SnTypeEnum {
    NEW("NEW", "新建"),
    LOADED("LOADED", "装载"),
    EMPTY("EMPTY", "空置"),
    END("END", "结束"),
    WIP("WIP", "在制"),
    RESERVED("RESERVED", "保留"),
    SCRAPPED("SCRAPPED", "报废"),
    DOWN("DOWN", "下架"),
    USED("used", "已上料"),
    UP("UP", "上架");

    private String code;
    private String desc;

    SnTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public String getDesc() {
        return this.desc;
    }

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static SnTypeEnum parseByCode(String code) {
        return Arrays.stream(SnTypeEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<SnTypeEnum> first = Arrays.stream(SnTypeEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
