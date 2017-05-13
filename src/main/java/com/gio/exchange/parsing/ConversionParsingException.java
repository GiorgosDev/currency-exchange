package com.gio.exchange.parsing;

import com.gio.exchange.CurrencyExchangeAppException;

public class ConversionParsingException extends CurrencyExchangeAppException{
    public ConversionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
