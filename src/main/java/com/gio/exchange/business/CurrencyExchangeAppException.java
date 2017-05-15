package com.gio.exchange.business;

public class CurrencyExchangeAppException extends RuntimeException{
    public CurrencyExchangeAppException(String message, Throwable cause) {
        super(message, cause);
    }

    public CurrencyExchangeAppException(String message) {
        super(message);
    }
}
