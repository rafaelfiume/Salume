package com.rafaelfiume.db.plugin.sqlscripts;

import com.rafaelfiume.db.plugin.Version;
import org.junit.Test;

import java.nio.file.FileSystems;

import static java.lang.String.format;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static support.Decorators.version;
import static support.Decorators.with;

public class ScriptFilesNavigatorTest {

    private static final String FILE_SEPARATOR = FileSystems.getDefault().getSeparator();

    private ScriptFilesNavigator subject;

    @Test
    public void returnsAllTheScriptFilesUnder_scripts_DirectoryWhen_No_MajorAndMinorVersionAreSpecified() {
        aFileNavigatorFor_Scripts_Dir(); // see resources/scripts folder

        hasAScriptFileNamed("scripts/i01/01.create-a-table-here.sql"                      , with(version("i01", "01")));
        hasAScriptFileNamed("scripts/i01/02.doing-something-script.sql"                   , with(version("i01", "02")));
        hasAScriptFileNamed("scripts/i01/03.doing-something-else-script.sql"              , with(version("i01", "03")));
        hasAScriptFileNamed("scripts/i02/01.create-another-table-in-another-iteration.sql", with(version("i02", "01")));
        thenHasNoMoreScriptFiles();
    }

    private void aFileNavigatorFor_Scripts_Dir() {
        this.subject = new ScriptFilesNavigator();
    }

    private void hasAScriptFileNamed(String expectedScriptName, Version expectedVersion) {
        String platformIndependentScriptName = expectedScriptName.replace("/", FILE_SEPARATOR);
        assertThat(format("expected script file %s", platformIndependentScriptName), subject.hasNext(), is(true));
        final Script script = subject.next();
        assertThat(script.name(), is(platformIndependentScriptName));
        assertThat(script.version(), is(expectedVersion));
    }

    private void thenHasNoMoreScriptFiles() {
        assertThat("expected no more script files", subject.hasNext(), is(false));
    }

}
