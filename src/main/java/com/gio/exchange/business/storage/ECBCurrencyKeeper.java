package com.gio.exchange.business.storage;

import com.gio.exchange.business.MessageConstants;
import com.gio.exchange.business.parsing.ConversionDataParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class ECBCurrencyKeeper implements CurrencyKeeper {

    @Value("${rate.days.expired}")
    private int daysExpired;

    @Value("${rate.daily.url}")
    private String rateDailyURL;

    @Value("${rate.90.days.url}")
    private String rateThreeMonthURL;


    private Map<LocalDate, Map<String,Float>> currencyRates = new ConcurrentHashMap<>();

    @Autowired
    private ConversionDataParser parser;

    @Override
    public void setDaysExpired(int days) {
        daysExpired = days;
    }

    @Override
    public void load() {
        try(InputStream input = new URL(rateThreeMonthURL).openStream()) {
            currencyRates = parser.parse(input);
        } catch (IOException e) {
            throw new ECBConnectionException(e.getMessage(), e);
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
        currencyRates.forEach((key, value) -> {if(isDateExpired(key)) datesToRemove.add(key);});
        datesToRemove.forEach(currencyRates::remove);
    }

    private boolean isDateExpired(LocalDate date){
        return LocalDate.now().minusDays(daysExpired).isAfter(date);
    }

    private void loadTodayRate(){
        try(InputStream input = new URL(rateDailyURL).openStream()) {
            currencyRates.putAll(parser.parse(input));
        } catch (IOException e) {
            throw new ECBConnectionException(e.getMessage(), e);
        }
    }


    @Override
    public Map<String, Float> getRatesForDate(LocalDate requestDate){
        if(isDateExpired(requestDate)){
            throw new ConversionNoDataException(MessageConstants.EXPIRED_DATE_MESSAGE);
        }
        if(requestDate.isAfter(LocalDate.now())){
            throw new ConversionNoDataException(MessageConstants.FUTURE_DATE_MESSAGE);
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
        if(isDateExpired(lastNonHolidayDate))
            throw new ConversionNoDataException(MessageConstants.EXPIRED_DATE_MESSAGE);
        return currencyRates.get(lastNonHolidayDate);
    }

    private boolean isDatePresentInCurrencyRatesAndNotExpired(LocalDate lastNonHolidayDate){
        return !currencyRates.containsKey(lastNonHolidayDate)
                && !isDateExpired(lastNonHolidayDate);
    }

    @Override
    public void setParser(ConversionDataParser parser) {
        this.parser = parser;
    }

    @Override
    public int getDaysExpired() {
        return daysExpired;
    }

    public void setRateDailyURL(String rateDailyURL) {
        this.rateDailyURL = rateDailyURL;
    }

    public void setRateThreeMonthURL(String rateThreeMonthURL) {
        this.rateThreeMonthURL = rateThreeMonthURL;
    }
}
