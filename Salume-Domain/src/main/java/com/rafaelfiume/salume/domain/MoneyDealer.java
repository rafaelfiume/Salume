package com.rafaelfiume.salume.domain;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.util.Locale;

public class MoneyDealer {

    private final String currencyCode = "EUR";
    private final Locale locale = Locale.ITALY;

    private final MonetaryAmountFormat monetaryAmountFormat = MonetaryFormats.getAmountFormat(locale);

    public MonetaryAmount theAmountOf(BigDecimal value) {
        return Money.of(value, currencyCode);
    }

    public MonetaryAmount theAmountOf(String value) {
        return monetaryAmountFormat.parse(value);
    }

    public String format(MonetaryAmount amount) {
        return monetaryAmountFormat.format(amount);
    }

    public BigDecimal numberFrom(MonetaryAmount amount) {
        return amount.getNumber().numberValue(BigDecimal.class);
    }
}
