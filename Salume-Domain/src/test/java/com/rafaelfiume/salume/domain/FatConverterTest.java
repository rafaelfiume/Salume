package com.rafaelfiume.salume.domain;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class FatConverterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private final FatConverter underTest = new FatConverter();

    @Test
    public void shouldReturnFatConfiguredForItalyFromString() {
        assertThat(underTest.theFatOf("32,33"), is(new Double("32.33")));
    }

    @Test
    public void shouldReturnFormattedStringConfiguredForItalyFromFat() {
        assertThat(underTest.format(32.33f), is("32,33"));
    }

    @Test
    public void shouldThrowExceptionWhenPassingNonSenseValueForFat() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Unparseable number: \"bananas\"");

        underTest.theFatOf("bananas");
    }

}
