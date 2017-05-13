package com.gio.exchange.business.keeper;

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
        Assert.assertEquals(ECBCurrencyParserTests.RESPONSE_RECORDS_NUMBER_90_DAYS, keeper.getRates().size());
    }

    @Test
    public void refreshDataTest(){
        keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(ECBCurrencyParserTests.SAMPLE_XML_TWO_DATES);
        keeper.setParser(parser);
        keeper.setExpiredDays(1);
        keeper.load();
        Assert.assertEquals(2, keeper.getRates().size());
        parser.setStubInputData(SAMPLE_XML_NEW_DATE);
        keeper.refresh();
        Assert.assertEquals(1, keeper.getRates().size());
        Assert.assertEquals(2, keeper.getRates().get(LocalDate.of(2017,5,15)).size());
    }

    private static final String SAMPLE_XML_NEW_DATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<gesmes:Envelope xmlns:gesmes=\"http://www.gesmes.org/xml/2002-08-01\" xmlns=\"http://www.ecb.int/vocabulary/2002-08-01/eurofxref\">\n" +
            "\t<gesmes:subject>Reference rates</gesmes:subject>\n" +
            "\t<gesmes:Sender>\n" +
            "\t\t<gesmes:name>European Central Bank</gesmes:name>\n" +
            "\t</gesmes:Sender>\n" +
            "\t<Cube>\n" +
            "\t\t<Cube time='2017-05-15'>\n" +
            "\t\t\t<Cube currency='USD' rate='1.0876'/>\n" +
            "\t\t\t<Cube currency='JPY' rate='123.82'/>\n" +
            "\t\t</Cube>\n" +
            "\t</Cube>\n" +
            "</gesmes:Envelope>";
}
