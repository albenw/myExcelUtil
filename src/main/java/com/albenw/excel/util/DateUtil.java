package com.albenw.excel.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 * @date 2019-01-04.
 */
public class DateUtil {

    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static Map<String, DateTimeFormatter> formatterMap = new ConcurrentHashMap<>();
    private static ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    public static String format(Date date, String format){
        if(date == null || StringUtil.isBlank(format)){
            return StringUtil.EMPTY;
        }
        LocalDateTime localDateTime = dateToLdt(date);
        DateTimeFormatter dateTimeFormatter = formatterMap.get(format);
        if(dateTimeFormatter == null){
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            formatterMap.put(format, dateTimeFormatter);
        }
        return dateTimeFormatter.format(localDateTime);
    }

    public static Date parse(String value, String format){
        DateTimeFormatter dateTimeFormatter = formatterMap.get(format);
        if(dateTimeFormatter == null){
            dateTimeFormatter = DateTimeFormatter.ofPattern(format);
            formatterMap.put(format, dateTimeFormatter);
        }
        LocalDateTime parse = LocalDateTime.parse(value, dateTimeFormatter);
        return ldtToDate(parse);
    }

    private static LocalDateTime dateToLdt(Date date){
        Instant instant = date.toInstant();
        return instant.atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    private static Date ldtToDate(LocalDateTime ldt){
        Instant instant = ldt.atZone(DEFAULT_ZONE_ID).toInstant();
        return Date.from(instant);
    }

}
