package com.gio.exchange.business.storage;

import com.gio.exchange.business.parsing.ConversionDataParser;

import java.time.LocalDate;
import java.util.Map;

public interface CurrencyKeeper {

    void setDaysExpired(int days);

    void load();

    void refresh();

    Map<String, Float> getRatesForDate(LocalDate requestDate);

    int getNumberOfDaysWithRates();

    void setParser(ConversionDataParser parser);

    int getDaysExpired();

}
