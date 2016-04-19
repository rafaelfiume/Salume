package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptFilesNavigator;
import com.rafaelfiume.db.plugin.support.ScriptsReader;
import com.rafaelfiume.db.plugin.support.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.support.VersionManager;
import org.apache.maven.plugin.logging.Log;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class DbIncrementorTest {

    private static final String PETITIONS_SCHEMA = "petitions";

    @Mock private SimpleJdbcDatabaseSupport dbSupport;
    @Mock private ScriptsReader scriptsReader;
    @Mock private Log log;
    @Mock private VersionManager versionManager;

    private DbIncrementor underTest;

    private String currentMajorVersion;
    private String currentMinorVersion;

    @Test @Ignore // wip
    public void updateDatabaseToNewestVersion() {
        given_currentMajorVersionIs("i01");
        given_currentMinorVersionIs("01");
        given(versionManager.majorVersion()).willReturn("i02");
        given(versionManager.minorVersion()).willReturn("01");
        givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir();

        // when...
        underTest.incrementDb(PETITIONS_SCHEMA);

        then(dbSupport).should(never()) .execute(scriptsFrom("scripts/i01/01.create-a-table-here.sql"));
        then(dbSupport).should(times(1)).execute(scriptsFrom("scripts/i01/02.doing-something-script.sql"));
        then(dbSupport).should(times(1)).execute(scriptsFrom("scripts/i01/03.doing-something-else-script.sql"));
        then(dbSupport).should(times(1)).execute(scriptsFrom("scripts/i02/01.create-another-table-in-another-iteration.sql"));
    }

    private void given_currentMajorVersionIs(String currentMajorVersion) {
        this.currentMajorVersion = currentMajorVersion;
    }

    private void given_currentMinorVersionIs(String currentMinorVersion) {
        this.currentMinorVersion = currentMinorVersion;
    }

    private void givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir() {
        this.underTest = new DbIncrementor(
                dbSupport,
                new ScriptFilesNavigator(), // will load script files in the resources/scripts dir
                scriptsReader,
                log
        );
    }

    private String scriptsFrom(String source) {
        return new ScriptsReader().readScript(source);
    }

    public static void main(String... args) {
        ScriptFilesNavigator nav = new ScriptFilesNavigator();

        while(nav.hasNext()) {
            System.out.println(nav.next());
        }
    }
}