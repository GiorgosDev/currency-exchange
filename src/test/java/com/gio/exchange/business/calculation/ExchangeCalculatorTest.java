package com.gio.exchange.business.calculation;

import com.gio.exchange.business.parsing.ECBCurrencySAXStubParser;
import com.gio.exchange.business.storage.ConversionNoDataException;
import com.gio.exchange.business.storage.CurrencyKeeper;
import com.gio.exchange.business.storage.ECBCurrencyKeeper;
import com.gio.exchange.business.vo.CurrencyExchangeRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ExchangeCalculatorTest {

    private static ExchangeCalculator calculator;

    @Before
    public void initCalculator(){
        CurrencyKeeper keeper = new ECBCurrencyKeeper();
        ECBCurrencySAXStubParser parser = new ECBCurrencySAXStubParser();
        parser.setStubInputData(SAMPLE_XML_NEW_DATE);
        keeper.setParser(parser);
        keeper.load();
        calculator = new ExchangeCalculatorImpl();
        ((ExchangeCalculatorImpl)calculator).setKeeper(keeper);
    }

    @Test
    public void calculateEurToUsd(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("EUR", "USD",
                LocalDate.now(), BigDecimal.valueOf(100d));
        BigDecimal result = calculator.calculate(request);
        Assert.assertEquals(BigDecimal.valueOf(108.76), result.stripTrailingZeros());
    }

    @Test
    public void calculateUsdToEur(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR",
                LocalDate.now(), BigDecimal.valueOf(100d));
        BigDecimal result = calculator.calculate(request);
        Assert.assertEquals(BigDecimal.valueOf(91.94557),result);
    }

    @Test
    public void calculateUsdToJpy(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "JPY",
                LocalDate.now(), BigDecimal.valueOf(100d));
        BigDecimal result = calculator.calculate(request);
        Assert.assertEquals(BigDecimal.valueOf(11384.7),result.stripTrailingZeros());
    }

    @Test(expected = ConversionNoDataException.class)
    public void calculateNonExistingCurrencyTo(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "AAA",
                LocalDate.now(), BigDecimal.valueOf(100d));
        calculator.calculate(request);
    }

    @Test(expected = ConversionNoDataException.class)
    public void calculateNonExistingCurrencyFrom(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("AAA", "EUR",
                LocalDate.now(), BigDecimal.valueOf(100d));
        calculator.calculate(request);
    }


    @Test(expected = ConversionNoDataException.class)
    public void calculateFutureDate(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR",
                LocalDate.now().plusDays(1), BigDecimal.valueOf(100d));
        calculator.calculate(request);
    }

    @Test(expected = ConversionNoDataException.class)
    public void calculateNoDataDate(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR",
                LocalDate.now().minusDays(2), BigDecimal.valueOf(100d));
        calculator.calculate(request);
    }

    @Test(expected = ConversionNoDataException.class)
    public void calculateVeryOldDate(){
        CurrencyExchangeRequest request = new CurrencyExchangeRequest("USD", "EUR",
                LocalDate.now().minusDays(91), BigDecimal.valueOf(100d));
        calculator.calculate(request);
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
