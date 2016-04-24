package com.rafaelfiume.db.plugin.sqlscripts;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ScriptsReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void retrieveScriptFile() {
        final String scripts = new ScriptsReader().readScript("scripts/i01/01.create-product-and-reputation-tables.sql");
        assertThat(scripts, isNotEmpty());
    }

    @Test
    public void failsWhenCanNotFindDbScripts() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("could not load scripts from scripts/inexistent.script.sql");

        new ScriptsReader().readScript("scripts/inexistent.script.sql");
    }

    private TypeSafeMatcher<String> isNotEmpty() {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String actual) {
                return isNotBlank(actual);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("a not empty string");
            }
        };
    }
}
