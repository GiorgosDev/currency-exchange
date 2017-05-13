package com.gio.exchange.business.keeper;

import com.gio.exchange.business.parsing.ConversionDataParser;

import java.util.Date;
import java.util.Map;

public interface CurrencyKeeper {

    void setExpiredDays(int days);

    void load();

    void refresh();

    Map<Date, Map<String,Float>> getRates();

    void setParser(ConversionDataParser parser);

}
