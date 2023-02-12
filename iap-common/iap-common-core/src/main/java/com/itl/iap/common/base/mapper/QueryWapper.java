package com.itl.iap.common.base.mapper;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @Author cjq
 * @Date 2021/10/15 9:51 上午
 * @Description TODO
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface QueryWapper {

    /**
     * 拼接的查询字段 可带别名
     *
     * @return
     */
    String value() default "";

    /**
     * 拼接的sql
     *
     * @return
     */
    String whereSql() default "";

    QueryWapperEnum queryWapperEnum() default QueryWapperEnum.EQ;
}
