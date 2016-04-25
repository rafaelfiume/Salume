package com.rafaelfiume.db.plugin.sqlscripts;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.rafaelfiume.db.plugin.sqlscripts.Script.newScript;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static support.Decorators.script;
import static support.Decorators.then;

public class ScriptsReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void retrieveScriptFile() {
        // given
        ScriptsReader scriptsReader = new ScriptsReader();

        // when
        String scripts = scriptsReader.read(newScript("scripts/i01/01.create-product-and-reputation-tables.sql"));

        then(scripts, isNotEmpty());
    }

    ///////////////////////////// Sad path... ///////////////////////////////

    @Test
    public void failsWhenCanNotFindDbScripts() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("could not load scripts from: scripts/i34/08/inexistent.script.sql");

        new ScriptsReader().read(script("scripts/i34/08/inexistent.script.sql"));
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
