package com.gio.exchange.model;

import com.gio.exchange.parsing.ConversionDataParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ECBCurrencyKeeper implements CurrencyKeeper {

    public static final String TODAY_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String NINETY_DAYS_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    private Map<Date, Map<String,Float>> currencyRates = new HashMap<>();

    @Autowired
    private ConversionDataParser parser;

    @Override
    public void load() {
        try(InputStream input = new URL(NINETY_DAYS_RATE_URL).openStream()) {
            currencyRates = parser.parse(input);
        } catch (IOException e) {
            throw new ECGConnectionException(e.getMessage(), e);
        }

    }

    @Override
    public void refresh() {
        //todo if there's data - remove last date, load new date, add new date

    }

    @Override
    public Map<Date, Map<String, Float>> getRates() {
        return currencyRates;
    }

    @Override
    public void setParser(ConversionDataParser parser) {
        this.parser = parser;
    }
}
