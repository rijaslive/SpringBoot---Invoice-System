package com.bolsadeideas.springboot.app.util;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateUtil {


    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM-dd-yyyy");

    public static OffsetDateTime getOffsetDateTime(LocalDate localDate, LocalTime localTime){
        Instant instant = Instant.now();
        ZoneId systemZone = ZoneId.systemDefault(); // my timezone
//        ZoneOffset systemOffset = systemZone.getRules().getOffset(instant);
        ZoneOffset zoneOffSet= ZoneOffset.of("+05:30");
        return OffsetDateTime.of(localDate,localTime,zoneOffSet);
    }

    public static String getDateToString(OffsetDateTime offsetDateTime){
        ZoneOffset zoneOffSet= ZoneOffset.of("+05:30");
        return offsetDateTime.withOffsetSameInstant(zoneOffSet).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }
    public static String getDateToString(LocalDateTime localDateTime){
        return localDateTime.format(DATE_TIME_FORMATTER);
    }

    public static OffsetDateTime getStringToDate(String date){
        if(StringUtils.isEmpty(date)){
            return getOffsetDateTime(LocalDate.now(),LocalTime.of(0,0));
        }
        return getOffsetDateTime(LocalDate.parse(date,DATE_TIME_FORMATTER),LocalTime.of(0,0));
    }

    public static OffsetDateTime getStringToEndDate(String date){
        if(StringUtils.isEmpty(date)){
            return getOffsetDateTime(LocalDate.now(),LocalTime.of(23,59));
        }
        return getOffsetDateTime(LocalDate.parse(date,DATE_TIME_FORMATTER),LocalTime.of(23,59));
    }
}
