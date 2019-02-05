package com.albenw.excel.util;

import org.apache.commons.compress.utils.Lists;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author alben.wong
 * @date 2019-01-04.
 */
public class DateUtil {

    public static String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    private static List<String> TRY_PARSE_PATTERNS = new ArrayList<String>(){{
        add(DEFAULT_DATE_TIME_FORMAT);
        add(DEFAULT_DATE_FORMAT);
        add("yyyy/MM/dd");
        add("yyyy/MM/dd HH:mm:ss");
        add("yyyy年MM月dd日");
    }};

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

    private static LocalDateTime dateToLdt(Date date){
        Instant instant = date.toInstant();
        return instant.atZone(DEFAULT_ZONE_ID).toLocalDateTime();
    }

    private static Date ldtToDate(LocalDateTime ldt){
        Instant instant = ldt.atZone(DEFAULT_ZONE_ID).toInstant();
        return Date.from(instant);
    }

    public static Date tryParse(final String str) throws ParseException{
        return parseDate(str, TRY_PARSE_PATTERNS.toArray(new String[TRY_PARSE_PATTERNS.size()]));
    }

    public static Date parseDate(final String str, final String... parsePatterns) throws ParseException {
        return parseDate(str, null, parsePatterns);
    }

    public static Date parseDate(final String str, final Locale locale, final String... parsePatterns) throws ParseException {
        return parseDateWithLeniency(str, locale, parsePatterns, true);
    }

    private static Date parseDateWithLeniency(
            final String str, final Locale locale, final String[] parsePatterns, final boolean lenient) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser;
        if (locale == null) {
            parser = new SimpleDateFormat();
        } else {
            parser = new SimpleDateFormat("", locale);
        }

        parser.setLenient(lenient);
        final ParsePosition pos = new ParsePosition(0);
        for (final String parsePattern : parsePatterns) {

            String pattern = parsePattern;

            // LANG-530 - need to make sure 'ZZ' output doesn't get passed to SimpleDateFormat
            if (parsePattern.endsWith("ZZ")) {
                pattern = pattern.substring(0, pattern.length() - 1);
            }

            parser.applyPattern(pattern);
            pos.setIndex(0);

            String str2 = str;
            // LANG-530 - need to make sure 'ZZ' output doesn't hit SimpleDateFormat as it will ParseException
            if (parsePattern.endsWith("ZZ")) {
                str2 = str.replaceAll("([-+][0-9][0-9]):([0-9][0-9])$", "$1$2");
            }

            final Date date = parser.parse(str2, pos);
            if (date != null && pos.getIndex() == str2.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }


}
