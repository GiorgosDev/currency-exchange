package com.gio.exchange.model;

import com.gio.exchange.parsing.ECBCurrencySAXParser;

import java.util.Date;
import java.util.Map;

public class ECBCurrencyKeeper implements CurrencyKeeper {

    public static final String TODAY_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String NINETY_DAYS_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    Map<Date, Map<String,Float>> currencyRates;
    ECBCurrencySAXParser parser = new ECBCurrencySAXParser();

    @Override
    public void load() {
        //todo if there's no data  - load 90 days

    }

    @Override
    public void refresh() {
        //todo if there's data - remove last date, load new date, add new date

    }

    @Override
    public Map<Date, Map<String, Float>> getRates() {
        return currencyRates;
    }
}
