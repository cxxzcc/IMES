package com.itl.iap.mes.api.common;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * 操作类型枚举
 * @author dengou
 * @date 2021/10/25
 */
@Getter
public enum OperateTypeEnum {

    /**新增*/
    ADD("add", "新增"),
    /**修改*/
    UPDATE("update", "修改"),
    /**指派*/
    ASSIGN("assign", "指派"),
    /**受理*/
    ACCEPT("accept", "受理"),
    /**执行*/
    EXECUTE("execute", "执行"),
    /**撤销*/
    CANCEL("cancel", "撤销"),
    ;


    /**
     * 状态码
     */
    private String code;

    /**
     * 描述
     */
    private String desc;

    OperateTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * 根据code转换， 没找到对应code会返回null,需要注意判空
     * */
    public static OperateTypeEnum parseByCode(String code) {
        return Arrays.stream(OperateTypeEnum.values())
                .filter(e-> StrUtil.equals(e.getCode(), code)).findFirst().orElse(null);
    }

    /**
     * 根据code转换desc， 没找到对应code会返回null,需要注意判空
     * */
    public static String parseDescByCode(String code) {
        Optional<OperateTypeEnum> first = Arrays.stream(OperateTypeEnum.values())
                .filter(e -> StrUtil.equals(e.getCode(), code)).findFirst();
        if(first.isPresent()) {
            return first.get().getDesc();
        }
        return null;
    }
}
