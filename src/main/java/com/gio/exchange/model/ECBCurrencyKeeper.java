package com.gio.exchange.model;

import com.gio.exchange.parsing.ECBCurrencySAXParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ECBCurrencyKeeper implements CurrencyKeeper {

    public static final String TODAY_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String NINETY_DAYS_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    Map<Date, Map<String,Float>> currencyRates = new HashMap<>();
    ECBCurrencySAXParser parser = new ECBCurrencySAXParser();

    @Override
    public void load() {
        try(InputStream input = new URL(NINETY_DAYS_RATE_URL).openStream()) {
            currencyRates = parser.parse(input);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
}
