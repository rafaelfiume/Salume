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
    public void shouldReturn_MonetaryAmount_ConfiguredForItaly_FromBigDecimal() {
        final BigDecimal value = new BigDecimal("23.44");
        assertThat(underTest.theAmountOf(value), is(Money.of(value, "EUR")));
    }

    @Test
    public void shouldReturn_MonetaryAmount_ConfiguredForItaly_FromString() {
        assertThat(underTest.theAmountOf("EUR 99,22"), is(Money.of(new BigDecimal("99.22"), "EUR")));
    }

    @Test
    public void shouldReturn_FormattedString_FromMonetaryAmount_ConfiguredForItaly() {
        final MonetaryAmount money = underTest.theAmountOf("EUR 99,22");
        assertThat(underTest.format(money), is("EUR 99,22"));
    }

    @Test
    public void shouldReturn_BigDecimal_FromMonetaryAmount_ConfiguredForItaly() {
        final MonetaryAmount money = underTest.theAmountOf("EUR 99,22");
        assertThat(underTest.numberFrom(money), is(new BigDecimal("99.22")));
    }

}
