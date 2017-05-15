package com.gio.exchange.business.storage;

import com.gio.exchange.business.CurrencyExchangeAppException;

public class ConversionNoDataException extends CurrencyExchangeAppException{
    public ConversionNoDataException(String message) {
        super(message);
    }
}
