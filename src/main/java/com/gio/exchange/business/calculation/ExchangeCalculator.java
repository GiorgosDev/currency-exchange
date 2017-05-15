package com.gio.exchange.business.calculation;

import com.gio.exchange.business.vo.CurrencyExchangeRequest;

import java.math.BigDecimal;

public interface ExchangeCalculator {

    BigDecimal calculate(CurrencyExchangeRequest request);

}
