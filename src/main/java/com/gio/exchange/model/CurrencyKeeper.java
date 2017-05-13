package com.gio.exchange.model;

import java.util.Date;
import java.util.Map;

public interface CurrencyKeeper {

    void load();

    void refresh();

    Map<Date, Map<String,Float>> getRates();

}
