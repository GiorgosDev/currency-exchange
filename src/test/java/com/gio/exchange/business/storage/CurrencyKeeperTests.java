package com.gio.exchange.business.storage;

import com.gio.exchange.business.parsing.ECBCurrencyParserTests;
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
    public void refreshDataTest(){
        keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(ECBCurrencyParserTests.SAMPLE_XML_TWO_DATES);
        keeper.setParser(parser);
        keeper.setDaysExpired(2);
        keeper.load();
        Assert.assertTrue(keeper.getRatesForDate(ECBCurrencyParserTests.TWO_DAYS_BEFORE).size() > 0);
        Assert.assertTrue(keeper.getRatesForDate(ECBCurrencyParserTests.THREE_DAYS_BEFORE).size() > 0);
        Assert.assertEquals(0, keeper.getRatesForDate(LocalDate.now()).size());
        parser.setStubInputData(SAMPLE_XML_NEW_DATE);
        keeper.refresh();
        Assert.assertTrue(keeper.getRatesForDate(LocalDate.now()).size() > 0);
    }

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
