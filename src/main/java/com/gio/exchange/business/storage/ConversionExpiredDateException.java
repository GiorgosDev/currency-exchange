package com.gio.exchange.business.storage;

import com.gio.exchange.CurrencyExchangeAppException;

public class ConversionExpiredDateException extends CurrencyExchangeAppException{
    public ConversionExpiredDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
