package com.itl.iap.common.base.utils;

import com.itl.iap.common.base.constants.DateScopeEnum;
import lombok.Data;

import java.time.*;

/**
 * 时间封装类
 */
public class LocalDateTimeUtils {

    public static final String MinTime = "T00:00:00";
    public static final String MaxTime = "T23:59:59.999999999";

    @Data
    public static class DateTimeScope {
        private LocalDateTime startTime;
        private LocalDateTime endTime;
    }

    /**
     * 根据时间类型获取时间范围
     *
     * @param timeType 0-今日 1-昨日 2-本周 3-本月
     * @return
     */
    public static DateTimeScope getDateTimeScope(String timeType) {
        DateTimeScope dateTimeScope = null;
        LocalDate now = LocalDate.now();
        if (DateScopeEnum.TODAY.getCode().equals(timeType)) {//今日
            dateTimeScope = getStartOrEndDayOfDay(now);
        } else if (DateScopeEnum.YESTERDAY.getCode().equals(timeType)) {//昨日
            dateTimeScope = getStartOrEndDayOfDay(now.minusDays(1));
        } else if (DateScopeEnum.WEEK.getCode().equals(timeType)) {//本周
            dateTimeScope = getStartOrEndDayOfWeek(now);
        } else if (DateScopeEnum.MONTH.getCode().equals(timeType)) {//本月
            dateTimeScope = getStartOrEndDayOfMonth(now);
        }
        return dateTimeScope;
    }

    /**
     * @Description:当天的开始时间结束时间
     */
    public static DateTimeScope getStartOrEndDayOfDay(LocalDate today) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        DateTimeScope scope = new DateTimeScope();
        scope.setStartTime(LocalDateTime.of(today, LocalTime.MIN));
        scope.setEndTime(LocalDateTime.of(today, LocalTime.MAX));
        return scope;
    }

    /**
     * @Description:本周的开始时间结束时间
     */
    public static DateTimeScope getStartOrEndDayOfWeek(LocalDate today) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        DayOfWeek week = today.getDayOfWeek();
        int value = week.getValue();

        DateTimeScope scope = new DateTimeScope();
        scope.setStartTime(LocalDateTime.parse(today.minusDays(value - 1) + time));
        scope.setEndTime(LocalDateTime.parse(today.plusDays(7 - value) + MaxTime));
        return scope;
    }

    /**
     * @Description:本月的开始时间结束时间
     */
    public static DateTimeScope getStartOrEndDayOfMonth(LocalDate today) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        int length = month.length(today.isLeapYear());
        DateTimeScope scope = new DateTimeScope();
        scope.setStartTime(LocalDateTime.parse(LocalDate.of(today.getYear(), month, 1) + time));
        scope.setEndTime(LocalDateTime.parse(LocalDate.of(today.getYear(), month, length) + MaxTime));
        return scope;
    }

    /**
     * @Description:本季度的开始时间结束时间
     */
    public static DateTimeScope getStartOrEndDayOfQuarter(LocalDate today) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        DateTimeScope scope = new DateTimeScope();
        scope.setStartTime(LocalDateTime.parse(LocalDate.of(today.getYear(), firstMonthOfQuarter, 1) + time));
        scope.setEndTime(LocalDateTime.parse(LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear())) + MaxTime));
        return scope;
    }

    /**
     * @Description:本年度的开始时间结束时间
     */
    public static DateTimeScope getStartOrEndDayOfYear(LocalDate today, Boolean isFirst) {
        String time = MinTime;
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        DateTimeScope scope = new DateTimeScope();
        scope.setStartTime(LocalDateTime.parse(LocalDate.of(today.getYear(), Month.JANUARY, 1) + time));
        scope.setEndTime(LocalDateTime.parse(LocalDate.of(today.getYear(), Month.DECEMBER, Month.DECEMBER.length(today.isLeapYear())) + MaxTime));
        return scope;
    }


}
