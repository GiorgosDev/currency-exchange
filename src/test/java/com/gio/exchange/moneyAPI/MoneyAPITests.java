package com.gio.exchange.moneyAPI;

import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.MonetaryAmount;
import javax.money.convert.ConversionQueryBuilder;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.MonetaryConversions;
import javax.money.convert.RateType;
import java.time.LocalDate;

public class MoneyAPITests {

    @Test
    public void getYesterdayCurrency(){
        MonetaryAmount inCHF = Money.of(100, "CHF");
        CurrencyConversion conv = MonetaryConversions.getConversion(ConversionQueryBuilder.of()
                .setProviders("ECB")
                .setTermCurrency("EUR")
                .set(MonetaryAmount.class, inCHF, MonetaryAmount.class)
                .set(LocalDate.of(2017, 4, 1))
                .setRateTypes(RateType.HISTORIC)
                .build());
        CurrencyConversion conv2 = MonetaryConversions.getConversion(ConversionQueryBuilder.of()
                .setProviders("ECB")
                .setTermCurrency("EUR")
                .set(MonetaryAmount.class, inCHF, MonetaryAmount.class)
                .set(LocalDate.of(2005, 5, 1))
                .setRateTypes(RateType.HISTORIC)
                .build());

        MonetaryAmount inEUR = inCHF.with(conv);
        System.out.print(inEUR);
        MonetaryAmount inEUR2 = inCHF.with(conv2);
        System.out.print(" "+ inEUR2);
    }
}
