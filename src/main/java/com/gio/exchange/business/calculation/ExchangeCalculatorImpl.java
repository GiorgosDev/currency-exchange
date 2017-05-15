package com.gio.exchange.business.calculation;

import com.gio.exchange.business.storage.ConversionNoDataException;
import com.gio.exchange.business.storage.CurrencyKeeper;
import com.gio.exchange.business.vo.CurrencyExchangeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;

@Component
public class ExchangeCalculatorImpl implements ExchangeCalculator {

    public static final int REFRESH_INTERVAL = 1000*60*10; //every ten minutes
    public static final String NO_CURRENCY_MESSAGE = "No data for requested currency present.";

    @Autowired
    CurrencyKeeper keeper;


    @Scheduled(fixedRate = REFRESH_INTERVAL)
    public void refreshData(){
        keeper.refresh();
    }

    @Override
    public BigDecimal calculate(CurrencyExchangeRequest request) {

        Map<String, Float> rates = keeper.getRatesForDate(request.getDate());
        final String currencyFrom = request.getCurrencyFrom();
        final String currencyTo = request.getCurrencyTo();
        if(rates.containsKey(currencyFrom) && rates.containsKey(currencyTo)){
            if(currencyFrom.equals(currencyTo))
                return request.getAmount();
            final MathContext rounding = new MathContext(7);
            final BigDecimal currencyFromRate = new BigDecimal(rates.get(currencyFrom));

            final BigDecimal currencyToRate = new BigDecimal(rates.get(currencyTo));
            BigDecimal amount = request.getAmount();
            return amount.multiply(currencyToRate).divide(currencyFromRate, rounding);
        } else throw new ConversionNoDataException(NO_CURRENCY_MESSAGE);

    }

    public void setKeeper(CurrencyKeeper keeper) {
        this.keeper = keeper;
    }
}
