package com.bolsadeideas.springboot.app.util;

import java.time.*;

public class DateUtil {

    public static OffsetDateTime getOffsetDateTime(LocalDate localDate, LocalTime localTime){
        Instant instant = Instant.now();
        ZoneId systemZone = ZoneId.systemDefault(); // my timezone
        ZoneOffset systemOffset = systemZone.getRules().getOffset(instant);

        return OffsetDateTime.of(localDate,localTime,systemOffset);
    }
}
