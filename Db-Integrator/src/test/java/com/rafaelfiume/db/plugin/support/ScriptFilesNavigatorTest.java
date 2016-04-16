package com.rafaelfiume.db.plugin.support;

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.file.FileSystems;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ScriptFilesNavigatorTest {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private ScriptFilesNavigator underTest;

    @Test
    public void shouldReturnAllTheScriptFilesUnder_scripts_Directory() throws URISyntaxException {
        aFileNavigatorFor_Scripts_Dir(); // see resources/scripts folder

        hasAScriptFileNamed("scripts/i01/01.create-a-table-here.sql");
        hasAScriptFileNamed("scripts/i01/02.doing-something-script.sql");
        hasAScriptFileNamed("scripts/i01/03.doing-something-else-script.sql");
        hasAScriptFileNamed("scripts/i02/01.create-another-table-in-another-iteration.sql");
        thenHasNoMoreScriptFiles();
    }

    private void aFileNavigatorFor_Scripts_Dir() {
        this.underTest = new ScriptFilesNavigator();
    }

    private void hasAScriptFileNamed(String scriptName) {
        String scriptNamePlatformIndependent = scriptName.replace("/", FILE_SEPARATOR);
        assertThat(format("expected script file %s", scriptNamePlatformIndependent), underTest.hasNext(), is(true));
        assertThat(underTest.next(), is(scriptNamePlatformIndependent));
    }

    private void thenHasNoMoreScriptFiles() {
        assertThat("expected no more script files", underTest.hasNext(), is(false));
    }

}
