package com.gio.exchange.parsing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;

public class ECBCurrencySAXStubParser implements ConversionDataParser {

    private ECBCurrencySAXParser parser = new ECBCurrencySAXParser();

    private String stubInputData;

    @Override
    public Map<Date, Map<String, Float>> parse(InputStream inputData) {
        return parser.parse(new ByteArrayInputStream(stubInputData.getBytes()));
    }

    public void setStubInputData(String stubInputData) {
        this.stubInputData = stubInputData;
    }
}
