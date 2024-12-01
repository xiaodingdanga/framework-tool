package com.lx.framework.tool.utils.base;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;

/**
 * @author xin.liu
 * @description 时间转换工具类
 * @date 2023-04-25  11:08
 * @Version 1.0
 */
public class TimeUtils {

    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.of("Asia/Shanghai");
    public static final DateTimeFormatter DEFAULT_YEAR_FORMATTER = DateTimeFormatter.ofPattern("yyyy");
    public static final DateTimeFormatter DEFAULT_MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    public static final DateTimeFormatter DEFAULT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DEFAULT_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    // -----时间格式转化-----
    // 年月日转化成时间戳
    public static long toTimestamp(int year, int month, int day) {
        return toTimestamp(LocalDate.of(year, month, day));
    }

    // 年月日时分秒转化成时间戳
    public static long toTimestamp(int year, int month, int day, int hour, int minute, int second) {
        return toTimestamp(LocalDateTime.of(year, month, day, hour, minute, second));
    }

    // Date 转化成时间戳
    public static long toTimestamp(Date date) {
        return date.toInstant().getEpochSecond();
    }

    // LocalDate 转化成时间戳
    public static long toTimestamp(LocalDate localDate) {
        return toTimestamp(localDate.atStartOfDay());
    }

    // LocalDateTime 转化成时间戳
    public static long toTimestamp(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(DEFAULT_ZONE_ID);
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    /**
     * 格式化日期字符串转化成时间戳
     * @param dateString yyyy-MM-dd HH:mm:ss 格式的时间字符串
     * @return
     */
    public static long toTimestamp(String dateString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateString, DEFAULT_TIME_FORMATTER);
        ZonedDateTime zonedDateTime = localDateTime.atZone(DEFAULT_ZONE_ID);
        Instant instant = zonedDateTime.toInstant();
        return instant.getEpochSecond();
    }

    /**
     * 时间戳转化成 LocalDateTime
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(long timestamp) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID);
    }


    /**
     * 时间戳转化成 LocalDateTime
     * @param localdatetime
     * @return
     */
    public static LocalDateTime toLocalDateTime(LocalDateTime localdatetime) {
        return localdatetime;
    }

    /**
     * 格式化日期字符串转化成 LocalDateTime
     * @param dateString yyyy-MM-dd HH:mm:ss 格式的时间字符串
     * @return
     */
    public static LocalDateTime toLocalDateTime(String dateString) {
        return LocalDateTime.parse(dateString, DEFAULT_TIME_FORMATTER);
    }

    /**
     * 自定义格式转换 LocalDateTime
     * @param dateString 时间字符串
     * @param pattern 格式
     * @return
     */
    public static LocalDateTime toLocalDateTime(String dateString, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateString, formatter);
    }

    /**
     * 格式化日期
     * @param date
     * @return  yyyy-MM-dd HH:mm:ss 格式的时间字符串
     */
    public static String format(Date date) {
        return DEFAULT_TIME_FORMATTER.format(date.toInstant());
    }

    /**
     * Date 转化成格式化日期字符串
     * @param date  日期
     * @param pattern 格式
     * @return
     */
    public static String format(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault());
        return formatter.format(date.toInstant());
    }

    /**
     * 时间戳转化成格式化日期字符串
     * @param timestamp 时间戳
     * @return  yyyy-MM-dd HH:mm:ss 格式的时间字符串
     */
    public static String format(long timestamp) {
        return format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID));
    }

    /**
     * 时间戳转化成格式化日期字符串
     * @param timestamp  时间戳
     * @param pattern  自定义的格式
     * @return
     */
    public static String format(long timestamp, String pattern) {
        return format(LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), DEFAULT_ZONE_ID), pattern);
    }

    // TemporalAccessor 转化成格式化日期字符串
    // TemporalAccessor 接口表示任何可以被解析为日期和时间的对象，比如 LocalDate、LocalDateTime、ZonedDateTime 等
    public static String format(TemporalAccessor dateTime) {
        return DEFAULT_TIME_FORMATTER.format(dateTime);
    }


    public static String format(TemporalAccessor dateTime, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(dateTime);
    }

    // -----时间获取-----

    // 获取当前时间戳（精确至毫秒，13位）
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    // 获取当前的年（仅数值）
    public static int getCurrentYearValue() {
        return LocalDate.now().getYear();
    }

    // 获取当前的月（仅数值）
    public static int getCurrentMonthValue() {
        return LocalDate.now().getMonthValue();
    }

    // 获取当前的日（仅数值）
    public static int getCurrentDayValue() {
        return LocalDate.now().getDayOfMonth();
    }

    // 获取当前的年（格式为 yyyy 的字符串）
    public static String getCurrentYear() {
        return String.valueOf(LocalDate.now().getYear());
    }

    // 获取当前的年月（格式为 yyyy-MM 的字符串）
    public static String getCurrentMonth() {
        return YearMonth.now().toString();
    }

    // 获取当前的年月日（格式为 yyyy-MM-dd 的字符串）
    public static String getCurrentDate() {
        return LocalDate.now().toString();
    }

    // 获取当前的年月日时分秒（格式为 yyyy-MM-dd HH:mm:ss 的字符串）
    public static String getCurrentDateTime() {
        return LocalDateTime.now().format(DEFAULT_TIME_FORMATTER);
    }

    // 获取指定年月的当月第一天（格式为 yyyy-MM-dd 的字符串）
    public static String getFirstDayOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.atDay(1).format(DEFAULT_DATE_FORMATTER);
    }

    // 获取指定年月的当月最后一天（格式为 yyyy-MM-dd 的字符串）
    public static String getLastDayOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.atEndOfMonth().format(DEFAULT_DATE_FORMATTER);
    }

    // 获取起始日期至今的所有年（格式为 yyyy 的字符串列表）
    public static List<String> getYearsUntilNow(int year, int month, int day) {
        return getUnitsUntilNow(year, month, day, ChronoUnit.YEARS);
    }

    // 获取起始日期至今的所有年月（格式为 yyyy-MM 的字符串列表）
    public static List<String> getMonthsUntilNow(int year, int month, int day) {
        return getUnitsUntilNow(year, month, day, ChronoUnit.MONTHS);
    }

    // 获取起始日期至今的所有年月日（格式为 yyyy-MM-dd 的字符串列表）
    public static List<String> getDaysUntilNow(int year, int month, int day) {
        return getUnitsUntilNow(year, month, day, ChronoUnit.DAYS);
    }

    // 获取起始日期至今的所有年/月/日（字符串列表）
    public static List<String> getUnitsUntilNow(int year, int month, int day, ChronoUnit unit) {
        DateTimeFormatter formatter = null;
        switch (unit) {
            case YEARS:
                formatter = DEFAULT_YEAR_FORMATTER;
                break;
            case MONTHS:
                formatter = DEFAULT_MONTH_FORMATTER;
                break;
            case DAYS:
                formatter = DEFAULT_DATE_FORMATTER;
                break;
            default:
                formatter = DEFAULT_DATE_FORMATTER;
                break;
        }
        List<String> results = new ArrayList<>();
        LocalDate startDate = LocalDate.of(year, month, day);
        LocalDate currentDate = LocalDate.now();
        while (startDate.isBefore(currentDate) || startDate.isEqual(currentDate)) {
            results.add(startDate.format(formatter));
            startDate = startDate.plus(1, unit);
        }
        return results;
    }

    // 获取起始日期至今，按固定天数进行切片的起止时间戳
    public static List<Map<String, Long>> getTimestampSlicesUntilNow(int year, int month, int day, int interval) {
        return getTimeSlicesUntilNow(year, month, day, interval, TimeUtils::toTimestamp);
    }

    // 获取起始日期至今，按固定天数进行切片的起止 LocalDateTime
    public static List<Map<String, LocalDateTime>> getLocalDateTimeSlicesUntilNow(int year, int month, int day, int interval) {
        return getTimeSlicesUntilNow(year, month, day, interval, TimeUtils::toLocalDateTime);
    }

    // 获取起始日期至今，按固定天数进行切片的起止格式化日期字符串
    public static List<Map<String, String>> getDateStringSlicesUntilNow(int year, int month, int day, int interval) {
        return getTimeSlicesUntilNow(year, month, day, interval, TimeUtils::format);
    }


    // 获取起始日期至今，按固定天数进行切片的起止日期，起止日期的类型通过 transformFunction 转化
    public static <R> List<Map<String, R>> getTimeSlicesUntilNow(int year, int month, int day, int interval, Function<LocalDateTime, R> transformFunction) {
        List<Map<String, R>> slices = new ArrayList<>();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.of(year, month, day, 0, 0,0);
        LocalDateTime endTime = startTime.plusDays(interval);

        while (startTime.isBefore(endTime)) {

            R transformedStartTime = transformFunction.apply(startTime);
            R transformedEndTime = transformFunction.apply(endTime);

            Map<String, R> params = new HashMap<String, R>(){{
                put("startTime", transformedStartTime);
                put("endTime", transformedEndTime);
            }};

            slices.add(params);

            startTime = startTime.plusDays(interval);
            endTime = endTime.plusDays(interval);
            if(now.isBefore(endTime)){
                endTime = now;
            }
        }

        return slices;
    }

    // 是否是闰年
    public boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public static void main(String[] args) {
        System.out.println(format(1709274416393L,"yyyy-MM-dd HH:mm:ss"));
    }

}