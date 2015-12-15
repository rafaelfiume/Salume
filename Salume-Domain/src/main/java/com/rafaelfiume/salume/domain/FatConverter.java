package com.rafaelfiume.salume.domain;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class FatConverter {

    private final Locale locale = Locale.ITALY;

    private final NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);

    public FatConverter() {
        numberFormat.setMinimumFractionDigits(2);
    }

    public String format(double fat) {
        return numberFormat.format(fat);
    }

    public Number theFatOf(String fat) {
        try {
            return numberFormat.parse(fat);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
