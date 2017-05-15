package com.gio.exchange.business.calculation;

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

    @Autowired
    CurrencyKeeper keeper;


    @Scheduled(fixedRate = REFRESH_INTERVAL)
    public void refreshData(){
        keeper.refresh();
    }

    @Override
    public BigDecimal calculate(CurrencyExchangeRequest request) {

        Map<String, Float> rates = keeper.getRatesForDate(request.getDate());
        final MathContext rounding = new MathContext(5);
        final String currencyFrom = request.getCurrencyFrom();
        final BigDecimal currencyFromRate = BigDecimal.valueOf(rates.get(currencyFrom))
                .round(rounding);
        final String currencyTo = request.getCurrencyTo();
        final BigDecimal currencyToRate = BigDecimal.valueOf(rates.get(currencyTo))
                .round(rounding);
        BigDecimal amount = request.getAmount();
        return amount.multiply(currencyFromRate, rounding).divide(currencyToRate, rounding);
    }

    public void setKeeper(CurrencyKeeper keeper) {
        this.keeper = keeper;
    }
}
