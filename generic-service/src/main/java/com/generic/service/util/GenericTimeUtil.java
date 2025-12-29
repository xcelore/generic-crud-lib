package com.generic.service.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class GenericTimeUtil {
    public static LocalDateTime getLocalDateTimeInUTC() {
        return LocalDateTime.now(ZoneOffset.UTC);
    }

    public static LocalDateTime getLocalDateTimeInIST() {
        return LocalDateTime.now(ZoneId.of("Asia/Kolkata"));
    }
}
