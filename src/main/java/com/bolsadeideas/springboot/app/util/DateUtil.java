package com.bolsadeideas.springboot.app.util;

import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateUtil {


    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM-dd-yyyy");

    private static OffsetDateTime getOffsetDateTime(LocalDate localDate, LocalTime localTime){
        ZoneOffset zoneOffSet= ZoneOffset.of("+05:30");
        return OffsetDateTime.of(localDate,localTime,zoneOffSet);
    }

    public static OffsetDateTime getOffsetDateTimeWithTime(int hour, int minutes ){
        return getOffsetDateTime().withHour(hour).withMinute(minutes).withSecond(0);
    }

    public static OffsetDateTime getOffsetDateTime(){
        return OffsetDateTime.now(ZoneId.of("Asia/Kolkata"));
    }

    public static String getDateToString(OffsetDateTime offsetDateTime){
        ZoneOffset zoneOffSet= ZoneOffset.of("+05:30");
        return offsetDateTime.withOffsetSameInstant(zoneOffSet).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
    }

    public static OffsetDateTime getStringToStartDate(String date){
        if(StringUtils.isEmpty(date)){
            return getOffsetDateTime().withHour(0).withMinute(0).withSecond(0);
        }
        return getOffsetDateTime(LocalDate.parse(date,DATE_TIME_FORMATTER),LocalTime.of(0,0));
    }

    public static OffsetDateTime getStringToEndDate(String date){
        if(StringUtils.isEmpty(date)){
            return getOffsetDateTime().withHour(23).withMinute(59).withSecond(59);
        }
        return getOffsetDateTime(LocalDate.parse(date,DATE_TIME_FORMATTER),LocalTime.of(23,59));
    }
}
