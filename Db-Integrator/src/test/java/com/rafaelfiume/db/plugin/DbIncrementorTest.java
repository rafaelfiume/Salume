package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.database.VersionBase;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptFilesNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import org.apache.maven.plugin.logging.Log;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static support.Decorators.script;
import static support.Decorators.to;
import static support.Decorators.version;

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
        given_current   (version("i01", "01"));
        given_updatingTo(version("i02", "01"));
        givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir();

        // when...
        subject.incrementDb(PETITIONS_SCHEMA, to(version("i02", "01")));

        // then the first script i01.01 shouldn't be executed since it has already been in the past
        then(db).should(never()).execute(scriptFrom("scripts/i01/01.create-a-table-here.sql"));
        then(versionBase).should(never()).updateMajorVersionTo(to(any()));
        then(versionBase).should(never()).updateMinorVersionTo(to(any()));

        // then other scripts should be executed till db reaches the desired major/minor version
        then(db).should(times(1)).execute(scriptFrom("scripts/i01/02.doing-something-script.sql"));
        then(versionBase).should().updateMinorVersionTo(to("02"));

        then(db).should(times(1)).execute(scriptFrom("scripts/i01/03.doing-something-else-script.sql"));
        then(versionBase).should().updateMinorVersionTo(to("03"));

        then(db).should(times(1)).execute(scriptFrom("scripts/i02/01.create-another-table-in-another-iteration.sql"));
        then(versionBase).should().updateMajorVersionTo(to("i02"));
        then(versionBase).should().updateMinorVersionTo(to("01"));
    }

    private void given_current(Version version) {
        given(versionBase.currentVersion()).willReturn(version);
    }

    private void given_updatingTo(Version version) {
        updateToVersion = version;
    }

    private void givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir() {
        this.subject = new DbIncrementor(db, versionBase, updateToVersion, scriptsReader, log);
    }

    // Decorators

    private String scriptFrom(String name) {
        return new ScriptsReader().read(script(name));
    }

    public static void main(String... args) {
        ScriptFilesNavigator nav = new ScriptFilesNavigator();

        while(nav.hasNext()) {
            System.out.println(nav.next());
        }
    }
}
