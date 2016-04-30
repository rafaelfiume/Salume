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
    private Version newVersion;
    private Version currentVersion;

    @Test
    public void returnsAllTheScriptFilesUnder_scripts_DirWhen_No_MajorAndMinorVersionAreSpecified() {
        aNavigatorFor_scripts_DirWithNo_MajorAndMinorVersion_Specified(); // see resources/scripts folder

        returnsAScriptFileNamed("scripts/i01/01.create-a-table-here.sql"                      , with(version("i01", "01")));
        returnsAScriptFileNamed("scripts/i01/02.doing-something-script.sql"                   , with(version("i01", "02")));
        returnsAScriptFileNamed("scripts/i01/03.doing-something-else-script.sql"              , with(version("i01", "03")));
        returnsAScriptFileNamed("scripts/i02/01.create-another-table-in-another-iteration.sql", with(version("i02", "01")));
        thenNothingElse();
    }

    @Test
    public void returnsScriptFilesUnder_scripts_DirAndBetweenCurrentAndNewVersions() {
        given_current   (version("i01", "02"));
        given_updatingTo(version("i02", "01"));

        aNavigatorFor_scripts_Dir();

        returnsAScriptFileNamed("scripts/i01/03.doing-something-else-script.sql"              , with(version("i01", "03")));
        returnsAScriptFileNamed("scripts/i02/01.create-another-table-in-another-iteration.sql", with(version("i02", "01")));
        thenNothingElse();
    }

    private void aNavigatorFor_scripts_DirWithNo_MajorAndMinorVersion_Specified() {
        this.subject = new ScriptFilesNavigator();
    }

    private void returnsAScriptFileNamed(String expectedScriptName, Version expectedVersion) {
        String platformIndependentScriptName = expectedScriptName.replace("/", FILE_SEPARATOR);
        assertThat(
                format("#hasNext() should have returned <true> for: %s", platformIndependentScriptName),
                subject.hasNext(), is(true)
        );
        Script script = subject.next();
        assertThat(script.name(), is(platformIndependentScriptName));
        assertThat(script.version(), is(expectedVersion));
    }

    private void thenNothingElse() {
        assertThat("expected no more script files", subject.hasNext(), is(false));
    }

    private void given_current(Version currentVersion) {
        this.currentVersion = currentVersion;
    }

    private void given_updatingTo(Version newVersion) {
        this.newVersion = newVersion;
    }

    private void aNavigatorFor_scripts_Dir() {
        this.subject = new ScriptFilesNavigator(currentVersion, newVersion);
    }

}
