package com.rafaelfiume.db.plugin.sqlscripts;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.rafaelfiume.db.plugin.sqlscripts.Script.newScript;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static support.Decorators.script;
import static support.Decorators.then;
import static support.Decorators.version;

public class ScriptTest {

    @Test
    public void retrieveScriptFile() {
        assertThat(script("scripts/i01/01.create-product-and-reputation-tables.sql").content(), isNotEmpty());
    }


    @Test
    public void returnsMajorAndMinorVersion() {
        assertThat(script("scripts/i34/02.a-script.sql").version(), is(version("i34", "02")));
    }

    @Test
    public void toStringReturnsFileName() {
        assertThat(script("scripts/i34/02.a-script.sql").toString(), is("scripts/i34/02.a-script.sql"));
    }

    @Test
    public void scriptsWithSameNameAreEquals() {
        assertThat(script("scripts/i34/02.a-script.sql"), is(equalTo(script("scripts/i34/02.a-script.sql"))));
    }

    ///////////////////////////// Sad path... ///////////////////////////////

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void failsWhenCanNotFindScriptFile() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("could not load scripts from: scripts/i34/08/inexistent.script.sql");

        newScript("scripts/i34/08/inexistent.script.sql").content();
    }

    @Test
    public void failsToInstantiateScriptWhenFilePathDoesNotContain_major_Version() {
        thrown.expect(IllegalArgumentException.class);

        Script.newScript("scripts/02.missing.major.version.sql");
    }

    @Test
    public void explainsTheReasonWhyItFailedToInstantiateScript() {
        thrown.expectMessage("could not retrieve version from: scripts/02.missing.major.version.sql");

        Script.newScript("scripts/02.missing.major.version.sql");
    }

    @Test
    public void failsToInstantiateScriptWhenFilePathDoesNotContain_minor_Version() {
        thrown.expect(IllegalArgumentException.class);

        Script.newScript("scripts/i24/missing.minor.version.sql");
    }

    @Test
    public void failsToInstantiateScriptWhenFilePathDoesNotStartWith_scripts() {
        thrown.expect(IllegalArgumentException.class);

        Script.newScript("i24/02.does.not.starts.with.scripts.sql");
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
