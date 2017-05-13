package com.gio.exchange.business.storage;

import com.gio.exchange.business.parsing.ConversionDataParser;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class ECBCurrencyKeeper implements CurrencyKeeper {

    private static final Logger logger = LogManager.getLogger(ECBCurrencyKeeper.class);

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
            logger.error(e.getMessage(),e);
            throw new ECGConnectionException(e.getMessage(), e);
        }

    }

    @Override
    public void refresh() {
        if(currencyRates.size() == 0)
            load();
        else {
            Set<LocalDate> datesToRemove = new HashSet<>();
            currencyRates.entrySet().stream().filter(e -> LocalDate.now().minusDays(daysExpired).isAfter(e.getKey()))
                    .forEach(e -> datesToRemove.add(e.getKey()));
            datesToRemove.forEach(e -> currencyRates.remove(e));
            try(InputStream input = new URL(TODAY_RATE_URL).openStream()) {
                currencyRates.putAll(parser.parse(input));
            } catch (IOException e) {
                logger.error(e.getMessage(),e);
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
