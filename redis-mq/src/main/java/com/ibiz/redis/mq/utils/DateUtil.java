package com.ibiz.redis.mq.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @auther 喻场
 * @date 2020/7/2411:51
 */
public class DateUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SSS");

    public static LocalDateTime getLocalDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 返回格式yyyy-MM-dd HH:mm:ss:SSS
     * @return  yyyy-MM-dd HH:mm:ss:SSS
     */
    public static String formatterNowTime() {
        return getLocalDateTime().format(FORMATTER);
    }
}
