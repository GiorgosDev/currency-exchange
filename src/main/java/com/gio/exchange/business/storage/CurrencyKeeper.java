package com.gio.exchange.business.storage;

import com.gio.exchange.business.parsing.ConversionDataParser;

import java.time.LocalDate;
import java.util.Map;

public interface CurrencyKeeper {

    void setExpiredDays(int days);

    void load();

    void refresh();

    Map<LocalDate, Map<String,Float>> getRates();

    void setParser(ConversionDataParser parser);

}
