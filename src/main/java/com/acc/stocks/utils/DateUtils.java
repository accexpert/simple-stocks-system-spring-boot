package com.acc.stocks.utils;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DateUtils {
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_SHORT = "yyyy-MM-dd";

    public static String getFormattedDateFromTimestamp(Timestamp date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);
        return simpleDateFormat.format(new Date(date.getTime()));
    }

    public static String getFormattedDateFromDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_SHORT);
        return simpleDateFormat.format(date);
    }

    public static String getFormattedDateFromTimestampLong(Timestamp date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        return simpleDateFormat.format(new Date(date.getTime()));
    }

    public static String getCurrentFormattedDateLong() {
        return getFormattedDateFromTimestampLong(new Timestamp(System.currentTimeMillis()));
    }

    public static String getCurrentFormattedDateShort() {
        return getFormattedDateFromTimestamp(new Timestamp(System.currentTimeMillis()));
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
