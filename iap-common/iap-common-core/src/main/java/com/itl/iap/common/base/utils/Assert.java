package com.itl.iap.common.base.utils;

import com.itl.iap.common.base.exception.BasicCommonExceptionDefinition;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.iap.common.base.exception.CommonExceptionDefinition;

/**
 * 抛CommonException的断言工具类
 * @author dengou
 * @date 2021/10/19
 */
public class Assert {


    /**
     * 判断condition是否满足，满足将抛出CommonException异常
     * @param condition 条件表达式
     * @param b 异常说明
     * */
    public static void valid(boolean condition, BasicCommonExceptionDefinition b) throws CommonException {
        if(true == condition) {
            throw new CommonException(b);
        }
    }

    /**
     * 判断condition是否满足，满足将抛出CommonException异常
     * @param condition 条件表达式
     * @param b 异常说明
     * */
    public static void valid(boolean condition, String b) throws CommonException {
        if(true == condition) {
            throw new CommonException(b, CommonExceptionDefinition.VERIFY_EXCEPTION);
        }
    }

    /**
     * 判断condition是否不满足，不满足将抛出CommonException异常
     * @param condition 条件表达式
     * @param b 异常说明
     * */
    public static void notValid(boolean condition, BasicCommonExceptionDefinition b) throws CommonException {
        if(false == condition) {
            throw new CommonException(b);
        }
    }

}
