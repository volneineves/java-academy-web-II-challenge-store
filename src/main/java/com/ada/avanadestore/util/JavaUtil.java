package com.ada.avanadestore.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaUtil {

    public static Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public static String doRegexPattern(Pattern pattern, String value) {
        Matcher matcher = pattern.matcher(value);

        StringBuilder sb = new StringBuilder();

        if (matcher.find()) {
            for (int i = 1; i <= matcher.groupCount(); i++) {
                sb.append(matcher.group(i));
                if (i < matcher.groupCount()) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }
}
