package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.VersionBase;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptFilesNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import org.apache.maven.plugin.logging.Log;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.rafaelfiume.db.plugin.Version.newVersion;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static support.Decorators.and;
import static support.Decorators.majorVersion;
import static support.Decorators.minorVersion;
import static support.Decorators.to;

@RunWith(MockitoJUnitRunner.class)
public class DbIncrementorTest {

    private static final String PETITIONS_SCHEMA = "petitions";

    @Mock private SimpleJdbcDatabaseSupport db;
    @Mock private ScriptsReader scriptsReader;
    @Mock private Log log;
    @Mock private VersionBase versionBase;

    private DbIncrementor subject;
    private Version updateToVersion;

    @Test @Ignore // wip
    public void updateDatabaseFromVersion_i01_01_ToVersion_i02_01() {
        given_current   (majorVersion("i01"), and(minorVersion("01")));
        given_updatingTo(majorVersion("i02"), and(minorVersion("01")));
        givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir();

        // when...
        subject.incrementDb(PETITIONS_SCHEMA);

        // then the first script i01.01 shouldn't be executed since it has already been in the past
        then(db).should(never()).execute(scriptsFrom("scripts/i01/01.create-a-table-here.sql"));
        then(versionBase).should(never()).updateMajorVersionTo(to(any()));
        then(versionBase).should(never()).updateMinorVersionTo(to(any()));

        // then other scripts should be executed till db reaches the desired major/minor version
        then(db).should(times(1)).execute(scriptsFrom("scripts/i01/02.doing-something-script.sql"));
        then(versionBase).should().updateMinorVersionTo(to("02"));

        then(db).should(times(1)).execute(scriptsFrom("scripts/i01/03.doing-something-else-script.sql"));
        then(versionBase).should().updateMinorVersionTo(to("03"));

        then(db).should(times(1)).execute(scriptsFrom("scripts/i02/01.create-another-table-in-another-iteration.sql"));
        then(versionBase).should().updateMajorVersionTo(to("i02"));
        then(versionBase).should().updateMinorVersionTo(to("01"));
    }

    private void given_current(String currentMajorVersion, String currentMinorVersion) {
        given(versionBase.currentVersion()).willReturn(newVersion(currentMajorVersion, currentMinorVersion));
    }

    private void given_updatingTo(String majorVersion, String minorVersion) {
        updateToVersion = newVersion(majorVersion, minorVersion);
    }

    private void givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir() {
        this.subject = new DbIncrementor(db, updateToVersion, scriptsReader, log);
    }

    // Decorators

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
