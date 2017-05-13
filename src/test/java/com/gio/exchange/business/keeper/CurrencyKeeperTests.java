package com.gio.exchange.business.keeper;

import com.gio.exchange.business.parsing.ECBCurrencyParserTests;
import com.gio.exchange.business.parsing.ECBCurrencySAXParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CurrencyKeeperTests {
    CurrencyKeeper keeper;

    @Before
    public void initKeeper(){
        keeper = new ECBCurrencyKeeper();
        keeper.setParser(new ECBCurrencySAXParser());
    }

    @Test
    public void loadDataTest(){
        keeper.load();
        Assert.assertEquals(ECBCurrencyParserTests.RESPONSE_RECORDS_NUMBER_90_DAYS, keeper.getRates().size());
    }

    @Test
    public void refreshDataTest(){
        keeper.refresh();
        Assert.assertEquals(1, keeper.getRates().size());
    }
}
