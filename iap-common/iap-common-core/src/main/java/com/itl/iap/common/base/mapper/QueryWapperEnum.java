package com.itl.iap.common.base.mapper;

/**
 * @Author cjq
 * @Date 2021/10/15 9:52 上午
 * @Description TODO
 */
public enum QueryWapperEnum {

    EQ(1),
    LIKE(2),//常规匹配% value %
    MATCH(3), //特殊处理带 * ,
    IN(4), //in ,
    APPLY(5); //拼接sql ,

    private final int value;

    QueryWapperEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
