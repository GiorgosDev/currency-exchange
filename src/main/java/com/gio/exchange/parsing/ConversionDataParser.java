package com.gio.exchange.parsing;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public interface ConversionDataParser {
    Map<Date, Map<String,Float>> parse(InputStream inputData);
}
