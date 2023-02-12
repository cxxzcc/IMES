package com.itl.iap.common.base.constants;


import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 设备状态
 */
@Getter
@AllArgsConstructor
public enum DeviceStateEnum {

    DY("1", "待用"),
    ZY("2", "在用"),
    DWX("3", "待维修"),
    WXZ("4", "维修中"),
    DBY("5", "待保养"),
    BYZ("6", "保养中"),
    DDJ("7", "待点检"),
    DJZ("8", "点检中"),
    TY("9", "停用");


    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;


    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static DeviceStateEnum parseByCode(String code) {
        return Arrays.stream(DeviceStateEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<DeviceStateEnum> first = Arrays.stream(DeviceStateEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
