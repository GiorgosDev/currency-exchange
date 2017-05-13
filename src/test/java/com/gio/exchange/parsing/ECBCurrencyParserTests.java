package com.gio.exchange.parsing;

import com.gio.exchange.model.ECBCurrencyKeeper;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ECBCurrencyParserTests {
    ConversionDataParser parser = new ECBCurrencySAXParser();

    //todo test content

    @Test
    public void parseToCubeFromURLDailyTest() throws IOException {
        Map<Date, Map<String,Float>> parsedData = fetchAndParseFromURL(ECBCurrencyKeeper.TODAY_RATE_URL);
        Assert.assertEquals(1, parsedData.size());
    }

    @Test
    public void parseToCubeFromURLHistoryTest() throws IOException {
        Map<Date, Map<String,Float>> parsedData = fetchAndParseFromURL(ECBCurrencyKeeper.NINETY_DAYS_RATE_URL);
        Assert.assertEquals(RESPONSE_RECORDS_NUMBER_90_DAYS, parsedData.size());
    }

    private Map<Date, Map<String,Float>> fetchAndParseFromURL(String url) throws IOException {
        Map<Date, Map<String,Float>> parsedData = new HashMap<>();
        try(InputStream input = new URL(url).openStream()){
            parsedData = parser.parse(input);
        }
        return parsedData;
    }

    @Test
    public void parseSaxFromStringOneDayTest() throws ParseException {
        Map<Date, Map<String,Float>> parsedData = parser.parse(new ByteArrayInputStream(SAMPLE_XML_ONE_DATE.getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals(1, parsedData.size());
    }

    @Test
    public void parseSaxFromStringTwoDaysTest() throws ParseException {
        Map<Date, Map<String,Float>> parsedData = parser.parse(new ByteArrayInputStream(SAMPLE_XML_TWO_DATES.getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals(2, parsedData.size());
    }

    @Test
    public void parseSaxFromStringContentTest() throws ParseException {
        Map<Date, Map<String,Float>> parsedData = parser.parse(new ByteArrayInputStream(SAMPLE_XML_TWO_DATES.getBytes(StandardCharsets.UTF_8)));
        Assert.assertEquals(2, parsedData.size());
    }

    @Test(expected = ConversionParsingException.class)
    public void parseSaxFromStringWrongDateTest() throws ParseException {
        parser.parse(new ByteArrayInputStream(SAMPLE_XML_WRONG_DATE.getBytes(StandardCharsets.UTF_8)));
    }

    @Test(expected = ConversionParsingException.class)
    public void parseSaxFromStringWrongRateTest() throws ParseException {
        parser.parse(new ByteArrayInputStream(SAMPLE_XML_WRONG_RATE.getBytes(StandardCharsets.UTF_8)));
    }

    public static final int RESPONSE_RECORDS_NUMBER_90_DAYS = 62;

    public static final String SAMPLE_XML_ONE_DATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2017-05-12'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t\t<Cube currency='BGN' rate='1.9558'/>\n" +
            "\t\t\t<Cube currency='CZK' rate='26.576'/>\n" +
            "\t\t\t<Cube currency='DKK' rate='7.4402'/>\n" +
            "\t\t\t<Cube currency='GBP' rate='0.84588'/>\n" +
            "\t\t\t<Cube currency='HUF' rate='310.24'/>\n" +
            "\t\t\t<Cube currency='PLN' rate='4.2170'/>\n" +
            "\t\t\t<Cube currency='RON' rate='4.5450'/>\n" +
            "\t\t\t<Cube currency='SEK' rate='9.6673'/>\n" +
            "\t\t\t<Cube currency='CHF' rate='1.0963'/>\n" +
            "\t\t\t<Cube currency='NOK' rate='9.3665'/>\n" +
            "\t\t\t<Cube currency='HRK' rate='7.4225'/>\n" +
            "\t\t\t<Cube currency='RUB' rate='62.3148'/>\n" +
            "\t\t\t<Cube currency='TRY' rate='3.9038'/>\n" +
            "\t\t\t<Cube currency='AUD' rate='1.4731'/>\n" +
            "\t\t\t<Cube currency='BRL' rate='3.4227'/>\n" +
            "\t\t\t<Cube currency='CAD' rate='1.4941'/>\n" +
            "\t\t\t<Cube currency='CNY' rate='7.5047'/>\n" +
            "\t\t\t<Cube currency='HKD' rate='8.4761'/>\n" +
            "\t\t\t<Cube currency='IDR' rate='14497.16'/>\n" +
            "\t\t\t<Cube currency='ILS' rate='3.9203'/>\n" +
            "\t\t\t<Cube currency='INR' rate='69.9395'/>\n" +
            "\t\t\t<Cube currency='KRW' rate='1226.52'/>\n" +
            "\t\t\t<Cube currency='MXN' rate='20.5521'/>\n" +
            "\t\t\t<Cube currency='MYR' rate='4.7243'/>\n" +
            "\t\t\t<Cube currency='NZD' rate='1.5892'/>\n" +
            "\t\t\t<Cube currency='PHP' rate='54.087'/>\n" +
            "\t\t\t<Cube currency='SGD' rate='1.5314'/>\n" +
            "\t\t\t<Cube currency='THB' rate='37.778'/>\n" +
            "\t\t\t<Cube currency='ZAR' rate='14.6336'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";


    public static final String SAMPLE_XML_TWO_DATES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2017-05-12'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t\t<Cube currency='BGN' rate='1.9558'/>\n" +
            "\t\t\t<Cube currency='CZK' rate='26.576'/>\n" +
            "\t\t\t<Cube currency='DKK' rate='7.4402'/>\n" +
            "\t\t\t<Cube currency='GBP' rate='0.84588'/>\n" +
            "\t\t\t<Cube currency='HUF' rate='310.24'/>\n" +
            "\t\t\t<Cube currency='PLN' rate='4.2170'/>\n" +
            "\t\t\t<Cube currency='RON' rate='4.5450'/>\n" +
            "\t\t\t<Cube currency='SEK' rate='9.6673'/>\n" +
            "\t\t\t<Cube currency='CHF' rate='1.0963'/>\n" +
            "\t\t\t<Cube currency='NOK' rate='9.3665'/>\n" +
            "\t\t\t<Cube currency='HRK' rate='7.4225'/>\n" +
            "\t\t\t<Cube currency='RUB' rate='62.3148'/>\n" +
            "\t\t\t<Cube currency='TRY' rate='3.9038'/>\n" +
            "\t\t\t<Cube currency='AUD' rate='1.4731'/>\n" +
            "\t\t\t<Cube currency='BRL' rate='3.4227'/>\n" +
            "\t\t\t<Cube currency='CAD' rate='1.4941'/>\n" +
            "\t\t\t<Cube currency='CNY' rate='7.5047'/>\n" +
            "\t\t\t<Cube currency='HKD' rate='8.4761'/>\n" +
            "\t\t\t<Cube currency='IDR' rate='14497.16'/>\n" +
            "\t\t\t<Cube currency='ILS' rate='3.9203'/>\n" +
            "\t\t\t<Cube currency='INR' rate='69.9395'/>\n" +
            "\t\t\t<Cube currency='KRW' rate='1226.52'/>\n" +
            "\t\t\t<Cube currency='MXN' rate='20.5521'/>\n" +
            "\t\t\t<Cube currency='MYR' rate='4.7243'/>\n" +
            "\t\t\t<Cube currency='NZD' rate='1.5892'/>\n" +
            "\t\t\t<Cube currency='PHP' rate='54.087'/>\n" +
            "\t\t\t<Cube currency='SGD' rate='1.5314'/>\n" +
            "\t\t\t<Cube currency='THB' rate='37.778'/>\n" +
            "\t\t\t<Cube currency='ZAR' rate='14.6336'/>\n" +
            "\t\t</Cube>\n" +
            "\t\t<Cube time='2017-05-13'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t\t<Cube currency='BGN' rate='1.9558'/>\n" +
            "\t\t\t<Cube currency='CZK' rate='26.576'/>\n" +
            "\t\t\t<Cube currency='DKK' rate='7.4402'/>\n" +
            "\t\t\t<Cube currency='GBP' rate='0.84588'/>\n" +
            "\t\t\t<Cube currency='HUF' rate='310.24'/>\n" +
            "\t\t\t<Cube currency='PLN' rate='4.2170'/>\n" +
            "\t\t\t<Cube currency='RON' rate='4.5450'/>\n" +
            "\t\t\t<Cube currency='SEK' rate='9.6673'/>\n" +
            "\t\t\t<Cube currency='CHF' rate='1.0963'/>\n" +
            "\t\t\t<Cube currency='NOK' rate='9.3665'/>\n" +
            "\t\t\t<Cube currency='HRK' rate='7.4225'/>\n" +
            "\t\t\t<Cube currency='RUB' rate='62.3148'/>\n" +
            "\t\t\t<Cube currency='TRY' rate='3.9038'/>\n" +
            "\t\t\t<Cube currency='AUD' rate='1.4731'/>\n" +
            "\t\t\t<Cube currency='BRL' rate='3.4227'/>\n" +
            "\t\t\t<Cube currency='CAD' rate='1.4941'/>\n" +
            "\t\t\t<Cube currency='CNY' rate='7.5047'/>\n" +
            "\t\t\t<Cube currency='HKD' rate='8.4761'/>\n" +
            "\t\t\t<Cube currency='IDR' rate='14497.16'/>\n" +
            "\t\t\t<Cube currency='ILS' rate='3.9203'/>\n" +
            "\t\t\t<Cube currency='INR' rate='69.9395'/>\n" +
            "\t\t\t<Cube currency='KRW' rate='1226.52'/>\n" +
            "\t\t\t<Cube currency='MXN' rate='20.5521'/>\n" +
            "\t\t\t<Cube currency='MYR' rate='4.7243'/>\n" +
            "\t\t\t<Cube currency='NZD' rate='1.5892'/>\n" +
            "\t\t\t<Cube currency='PHP' rate='54.087'/>\n" +
            "\t\t\t<Cube currency='SGD' rate='1.5314'/>\n" +
            "\t\t\t<Cube currency='THB' rate='37.778'/>\n" +
            "\t\t\t<Cube currency='ZAR' rate='14.6336'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";

    private static final String SAMPLE_XML_WRONG_DATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2017-05'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";

    private static final String SAMPLE_XML_WRONG_RATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2017-05-12'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.a'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";

}
