package com.ruoyi.tools.utils.date;


import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * 时间处理工具类
 * LocalDate与数据库日期类型的对应关系：
 * 数据库           JAVA类
 * DATETIME        LocalDateTime
 * TIMESTAMP       LocalDateTime
 * DATE            LocalDate
 * TIME            LocalTime
 *
 * @author wocurr.com
 */
public class LocalDateTimeUtil {

    public static final String FORMAT_YYYYMMDD = "yyyyMMdd";
    public static final String FORMAT_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String FORMAT_YYYYMM = "yyyy/MM";
    public static final String FORMAT_YMD = "yyyy/MM/dd";
    public static final String FORMAT_MD = "MMdd";
    public static final String FORMAT_MDH = "MMddHH";
    public static final String FORMAT_MDHMSS = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_YMDHMS = "yyMMddHH:mm:ss";

    /**
     * 获取当前时间
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 格式化当前时间
     *
     * @return
     */
    public static String formatNow(String pattern) {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取指定时间的指定格式
     *
     * @return yyyy-MM-dd HH:mm:ss:SSS  (HH是24小时制，而hh是12小时制, ss是秒，SSS是毫秒)
     */
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 将字符串转为LocalDateTime类型
     *
     * @param time    时间字符串
     * @param pattern 时间格式
     * @return
     */
    public static LocalDateTime parseTime(String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取指定时间是周几
     */
    public static int week(LocalDateTime time) {
        return time.getDayOfWeek().getValue();
    }

    /**
     * 获取当前月份
     * @return
     */
    public static int month() {
        return now().getMonthValue();
    }

    /**
     * 获取月份
     * @param time
     * @return
     */
    public static int month(LocalDateTime time) {
        return time.getMonthValue();
    }

    /**
     * 获取加或减N月的第一天
     */
    public static LocalDateTime monthFirst(int num) {
        LocalDateTime newTime = plus(LocalDateTime.now(), num, ChronoUnit.MONTHS);
        newTime = newTime.with(TemporalAdjusters.firstDayOfMonth());
        return getDayStart(newTime);
    }

    /**
     * 获取加或减N月的最后天
     */
    public static LocalDateTime monthLast(int num) {
        LocalDateTime newTime = plus(LocalDateTime.now(), num, ChronoUnit.MONTHS);
        newTime = newTime.with(TemporalAdjusters.lastDayOfMonth());
        return getDayEnd(newTime);
    }


    /**
     * 获取加或减N周的第一天
     */
    public static LocalDateTime weekFirst(int num) {
        int week = week(LocalDateTime.now());
        LocalDateTime newTime = subtract(LocalDateTime.now(), week - 1, ChronoUnit.DAYS);
        newTime = plus(newTime, num * 7, ChronoUnit.DAYS);
        return getDayStart(newTime);
    }

    /**
     * 获取加或减N周的最后一天
     */
    public static LocalDateTime weekLast(int num) {
        int week = week(LocalDateTime.now());
        LocalDateTime newTime = plus(LocalDateTime.now(), 7 - week, ChronoUnit.DAYS);
        newTime = plus(newTime, num * 7, ChronoUnit.DAYS);
        return getDayEnd(newTime);
    }

    /**
     * 判断时间<br>
     *
     * @return t1 < t2 = true
     */
    public static boolean isBefore(LocalDateTime t1, LocalDateTime t2) {
        return t1.isBefore(t2);
    }

    /**
     * 判断时间
     *
     * @return t1 > t2 = true
     */
    public static boolean isAfter(LocalDateTime t1, LocalDateTime t2) {
        return t1.isAfter(t2);
    }


    /**
     * java.util.Date 转 LocalDateTime
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Timestamp 转 LocalDateTime
     *
     * @param timestamp
     * @return
     */
    public static LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp.toLocalDateTime();
    }

    /**
     * LocalDateTime 转 Timestamp
     *
     * @param localDateTime
     * @return
     */
    public static Timestamp toTimestamp(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Timestamp.from(instant);
    }

    /**
     * java.util.Date 转 Timestamp
     * @param date
     * @return
     */
    public static Timestamp toTimestamp(Date date) {
        return new Timestamp(date.getTime());
    }

    /**
     * LocalDateTime 转 java.util.Date
     */
    public static Date toDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }



    /**
     * 获取指定日期的毫秒
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    // 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    // 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
    public static LocalDateTime subtract(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }


    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param field 单位(年月日时分秒)
     **/
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }


    /**
     * 获取指定某一天的开始时间 00:00:00
     *
     * @return
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }


    /**
     * 获取指定某一天的结束时间  23:59:59.999
     *
     * @return
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time
                //.withDayOfMonth(1)    // 月
                //.withDayOfYear(2)     // 天
                .withHour(23)           // 时
                .withMinute(59)         // 分
                .withSecond(59)         // 秒
                .withNano(999999999);   // 毫秒（这里精确到9位数）
    }

    /**
     * 获取本周周一
     */
    public static LocalDateTime getWeekOfFirst(LocalDateTime time) {
        return time.with(TemporalAdjusters.previous(DayOfWeek.SUNDAY)).
                plusDays(1).withHour(0).withMinute(0).withSecond(0);
    }

    /**
     * 获取本周周日
     */
    public static LocalDateTime getWeekOfLast(LocalDateTime time) {
        return time.with(TemporalAdjusters.next(DayOfWeek.MONDAY)).
                minusDays(1).withHour(23).withMinute(59).withSecond(59);
    }

    /**
     * 获取本月第一天
     */
    public static LocalDateTime getMonthOfFirst(LocalDateTime time) {
        LocalDateTime firstday = time.with(TemporalAdjusters.firstDayOfMonth());
        return LocalDateTime.of(firstday.toLocalDate(), LocalTime.MIN);
    }

    /**
     * 获取本月最后一天
     */
    public static LocalDateTime getMonthOfLast(LocalDateTime time) {
        LocalDateTime lastDay = time.with(TemporalAdjusters.lastDayOfMonth());
        return LocalDateTime.of(lastDay.toLocalDate(), LocalTime.MAX);
    }

}
 
