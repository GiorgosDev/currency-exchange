package com.gio.exchange.business.keeper;

import com.gio.exchange.business.parsing.ConversionDataParser;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class ECBCurrencyKeeper implements CurrencyKeeper {

    private int daysExpired = 90;

    public static final String TODAY_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    public static final String NINETY_DAYS_RATE_URL = "http://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    private Map<LocalDate, Map<String,Float>> currencyRates = new HashMap<>();

    @Autowired
    private ConversionDataParser parser;

    @Override
    public void setExpiredDays(int days) {
        daysExpired = days;
    }

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
        if(currencyRates.size() == 0)
            load();
        else {
            currencyRates.entrySet().stream().filter(e -> LocalDate.now().minusDays(daysExpired).isAfter(e.getKey())).forEach(e -> currencyRates.remove(e.getKey()));
            try(InputStream input = new URL(TODAY_RATE_URL).openStream()) {
                currencyRates.putAll(parser.parse(input));
            } catch (IOException e) {
                throw new ECGConnectionException(e.getMessage(), e);
            }
        }


    }

    @Override
    public Map<LocalDate, Map<String, Float>> getRates() {
        return currencyRates;
    }

    @Override
    public void setParser(ConversionDataParser parser) {
        this.parser = parser;
    }
}
