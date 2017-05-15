package com.gio.exchange.business.storage;

import com.gio.exchange.CurrencyExchangeAppException;

public class ECBConnectionException extends CurrencyExchangeAppException{
    public ECBConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
