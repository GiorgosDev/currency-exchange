package com.gio.exchange.business.parsing;

import com.gio.exchange.CurrencyExchangeAppException;

public class ConversionParsingException extends CurrencyExchangeAppException{
    public ConversionParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
