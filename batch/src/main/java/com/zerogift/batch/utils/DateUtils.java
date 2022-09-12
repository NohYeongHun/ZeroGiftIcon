package com.zerogift.batch.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateUtils {

    private static final String MINUTE = "분";
    private static final String SECOND = "초";
    private static final String DELIMETER = "/";

    public static LocalDateTime getLocalDateParse(Date date) {
        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime();
    }

    public static String executeTime(LocalDateTime startDate, LocalDateTime endDate) {
        String minutes = ChronoUnit.MINUTES.between(startDate, endDate) + MINUTE;
        String seconds = ChronoUnit.SECONDS.between(startDate, endDate) % 60 + SECOND;

        return minutes + DELIMETER + seconds;
    }

}
