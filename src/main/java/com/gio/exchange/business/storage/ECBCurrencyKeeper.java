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
import java.util.*;

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
    public void setDaysExpired(int days) {
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
            if(currencyRates.containsKey(LocalDate.now())) // no need to refresh
                return;
            removeExpiredRates();
            loadTodayRate();
        }
    }

    private void removeExpiredRates(){
        Set<LocalDate> datesToRemove = new HashSet<>();
        currencyRates.entrySet().stream().filter(e -> LocalDate.now().minusDays(daysExpired).isAfter(e.getKey()))
                .forEach(e -> datesToRemove.add(e.getKey()));
        datesToRemove.forEach(e -> currencyRates.remove(e));
    }

    private void loadTodayRate(){
        try(InputStream input = new URL(TODAY_RATE_URL).openStream()) {
            currencyRates.putAll(parser.parse(input));
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            throw new ECGConnectionException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Float> getRatesForDate(LocalDate requestDate){
        if(LocalDate.now().minusDays(daysExpired).isAfter(requestDate)){
            return Collections.emptyMap();
        }
        return getRateForNonHolidayDate(requestDate);
    }

    @Override
    public int getNumberOfDaysWithRates() {
        return currencyRates.size();
    }

    private Map<String, Float> getRateForNonHolidayDate(LocalDate requestDate){
        LocalDate lastNonHolidayDate = requestDate;
        while (isDatePresentInCurrencyRatesAndNotExpired(lastNonHolidayDate) ){
            lastNonHolidayDate = lastNonHolidayDate.minusDays(1);
        }
        return currencyRates.get(lastNonHolidayDate);
    }

    private boolean isDatePresentInCurrencyRatesAndNotExpired(LocalDate lastNonHolidayDate){
        return !currencyRates.containsKey(lastNonHolidayDate)
                && LocalDate.now().minusDays(daysExpired).isBefore(lastNonHolidayDate);
    }

    @Override
    public void setParser(ConversionDataParser parser) {
        this.parser = parser;
    }

    @Override
    public int getDaysExpired() {
        return daysExpired;
    }
}
