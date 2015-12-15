package com.rafaelfiume.salume.domain;

import org.javamoney.moneta.Money;
import org.junit.Test;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class MoneyDealerTest {

    private final MoneyDealer underTest = new MoneyDealer();

    @Test
    public void shouldReturnMonetaryAmountConfiguredForItalyFromBigDecimal() {
        final BigDecimal value = new BigDecimal("23.44");
        assertThat(underTest.theAmountOf(value), is(Money.of(value, "EUR")));
    }

    @Test
    public void shouldReturnMonetaryAmountConfiguredForItalyFromString() {
        assertThat(underTest.theAmountOf("EUR 99,22"), is(Money.of(new BigDecimal("99.22"), "EUR")));
    }

    @Test
    public void shouldReturnFormattedStringConfiguredForItalyFromMonetaryAmount() {
        final MonetaryAmount money = underTest.theAmountOf("EUR 99,22");
        assertThat(underTest.format(money), is("EUR 99,22"));
    }

}
