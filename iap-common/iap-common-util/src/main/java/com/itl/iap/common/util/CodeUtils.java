package com.itl.iap.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 获取code的工具类
 * @author: LiShuaiPeng
 * @datetime 2019/10/22 14:16
 * @since JDK 1.8
 */
public class CodeUtils {
    /**
     * 最大循环生成code次数
     */
    public static final Integer NUM = 20;
    private static final String STR_DATE = "yyyyMMdd";

    /**
     * 生成账号或者密码
     * @param num 账号或密码的长度
     * @return
     */
    public static String getCode(Integer num) {
        return String.valueOf(RandomNumber.buildRandom(num));
    }

    /**
     * 获取随机编码
     * @param prefix
     * @param date
     * @return
     */
    public static String dateToCode(String prefix, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(STR_DATE);
        return prefix + sdf.format(date) + RandomNumber.buildRandom(4);
    }
}
