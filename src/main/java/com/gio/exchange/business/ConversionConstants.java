package com.gio.exchange.business;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ConversionConstants {
    public static final String DATE_ATTRIBUTE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_ATTRIBUTE_FORMAT).withZone(ZoneId.of("CET"));
}
