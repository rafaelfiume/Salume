package com.rafaelfiume.db.plugin.support;

import org.junit.Test;

import java.net.URISyntaxException;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ScriptFilesNavigatorTest {

    private ScriptFilesNavigator nav;

    @Test
    public void shouldReturnAllTheScriptFilesUnder_scripts_Directory() throws URISyntaxException {
        givenAFileNavigatorFor_Scripts_Dir(); // see resources/scripts folder

        hasAScriptFileNamed("scripts/i01/01.create-a-table-here.sql");
        hasAScriptFileNamed("scripts/i01/02.doing-something-script.sql");
        hasAScriptFileNamed("scripts/i01/03.doing-something-else-script.sql");
        hasAScriptFileNamed("scripts/i02/01.create-another-table-in-another-iteration.sql");

        hasNoMoreScriptFiles();
    }

    private void givenAFileNavigatorFor_Scripts_Dir() {
        this.nav = new ScriptFilesNavigator();
    }

    private void hasAScriptFileNamed(String scriptName) {
        assertThat(format("expected script file %s", scriptName), nav.hasNext(), is(true));
        assertThat(nav.next(), is(scriptName));
    }

    private void hasNoMoreScriptFiles() {
        assertThat("expected no more script files", nav.hasNext(), is(false));
    }

}
