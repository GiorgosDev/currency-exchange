package com.gio.exchange.business.storage;

import com.gio.exchange.CurrencyExchangeAppException;

public class ECGConnectionException extends CurrencyExchangeAppException{
    public ECGConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
