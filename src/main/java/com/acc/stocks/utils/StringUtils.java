package com.acc.stocks.utils;

public class StringUtils {
    public StringUtils() {}

    public static String formatLine(String line, int length) {
        if(null == line) line = org.apache.commons.lang3.StringUtils.EMPTY;
        return String.format("%-"+length+"s", line.length()>length ? line.substring(0, length) : line);
    }
}
