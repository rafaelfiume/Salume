package com.rafaelfiume.db.plugin.support;

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.FileSystems;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ScriptFilesNavigatorTest {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private ScriptFilesNavigator nav;

    @Test
    public void shouldReturnAllTheScriptFilesUnder_scripts_Directory() throws URISyntaxException {
        givenAFileNavigatorFor_Scripts_Dir(); // see resources/scripts folder

        hasAScriptFileNamed("scripts" + FILE_SEPARATOR + "i01" + FILE_SEPARATOR + "01.create-a-table-here.sql");
        hasAScriptFileNamed("scripts" + FILE_SEPARATOR + "i01" + FILE_SEPARATOR + "02.doing-something-script.sql");
        hasAScriptFileNamed("scripts" + FILE_SEPARATOR + "i01" + FILE_SEPARATOR + "03.doing-something-else-script.sql");
        hasAScriptFileNamed("scripts" + FILE_SEPARATOR + "i02" + FILE_SEPARATOR + "01.create-another-table-in-another-iteration.sql");

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
