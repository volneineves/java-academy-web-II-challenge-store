package com.ada.avanadestore.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class JavaUtil {

    public static Date convertToDate(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }
}
