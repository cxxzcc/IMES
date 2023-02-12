package com.itl.iap.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具
 *
 * @author Linjl
 * @date 2020-06-12
 * @since jdk1.8
 */
public class DateUtil {

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return new Date();
    }

    /**
     * 获取格式化时间（yyyy-MM-dd HH:mm:ss）
     *
     * @param timestamp 时间戳
     * @return String
     */
    public static String date(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * 日期类型转字符串
     *
     * @param date
     * @return String
     */
    public static String dateToString(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    /**
     * 日期类型转字符串(转化为yyyyMMdd格式)
     *
     * @param date
     * @return String
     */
    public static String dateToYyyyMmDd(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    /**
     * 日期类型转字符串(转化为yyyyMM格式)
     *
     * @param date
     * @return String
     */
    public static String dateToYyyyMm(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        return sdf.format(date);
    }

    /**
     * 日期格式转化
     *
     * @param date
     * @param format
     * @return String
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     *
     *
     * @param modifyDate
     * @return String
     */
    public static Date stringToDate(String modifyDate) {
        DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = fmt.parse(modifyDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期字符串转换
     *
     * @param dateStr
     * @return
     */
    public static Date dateStrConversion(String dateStr) {
        SimpleDateFormat simpleDateFormat;
        if (dateStr.length() > 10) {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } else if (dateStr.length() == 8) {
            simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        } else {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        }
        Date dateTime = null;
        try {
            dateTime = simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
}
