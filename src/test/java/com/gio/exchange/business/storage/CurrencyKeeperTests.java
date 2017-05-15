package com.gio.exchange.business.storage;

import com.gio.exchange.business.parsing.ECBCurrencySAXParser;
import com.gio.exchange.business.parsing.ECBCurrencySAXStubParser;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;

public class CurrencyKeeperTests {
    CurrencyKeeper keeper;


    @Test
    public void loadDataTest(){
        keeper = new ECBCurrencyKeeper();
        keeper.setParser(new ECBCurrencySAXParser());
        keeper.load();
        Assert.assertTrue(keeper.getNumberOfDaysWithRates() > 0);
    }

    @Test
    public void getPreviousDateRate(){
        keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(SAMPLE_XML_TWO_DATES);
        keeper.setParser(parser);
        keeper.setDaysExpired(2);
        keeper.load();
        Assert.assertEquals(32, keeper.getRatesForDate(LocalDate.now()).size());
    }

    @Test(expected = ConversionNoDataException.class)
    public void getFutureDateRate(){
        keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(SAMPLE_XML_TWO_DATES);
        keeper.setParser(parser);
        keeper.setDaysExpired(2);
        keeper.load();
        keeper.getRatesForDate(LocalDate.now().plusDays(1));
    }


    @Test
    public void refreshDataTest(){
        keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(SAMPLE_XML_TWO_DATES);
        keeper.setParser(parser);
        keeper.setDaysExpired(3);
        keeper.load();
        Assert.assertEquals(32, keeper.getRatesForDate(TWO_DAYS_BEFORE).size());
        Assert.assertEquals(32, keeper.getRatesForDate(THREE_DAYS_BEFORE).size());
        Assert.assertEquals(2, keeper.getNumberOfDaysWithRates());
        parser.setStubInputData(SAMPLE_XML_NEW_DATE);
        keeper.setDaysExpired(1);
        keeper.refresh();
        Assert.assertEquals(3, keeper.getRatesForDate(LocalDate.now()).size());
        Assert.assertEquals(1, keeper.getNumberOfDaysWithRates());
        Assert.assertEquals(0, keeper.getRatesForDate(TWO_DAYS_BEFORE).size());
        Assert.assertEquals(0, keeper.getRatesForDate(THREE_DAYS_BEFORE).size());
    }

    //todo test exceptions on future date or expired date

    //todo move same blocks to init methods

    //todo replace magic numbers

    private static final LocalDate TWO_DAYS_BEFORE = LocalDate.now().minusDays(2);
    private static final LocalDate THREE_DAYS_BEFORE = LocalDate.now().minusDays(3);


    private static final String SAMPLE_XML_TWO_DATES = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='" + TWO_DAYS_BEFORE + "'>\n" +
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
            "\t\t<Cube time='"+ THREE_DAYS_BEFORE +"'>\n" +
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

    private static final String SAMPLE_XML_NEW_DATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='" + LocalDate.now() + "'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";
}
