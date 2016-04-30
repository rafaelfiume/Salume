package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.database.VersionBase;
import org.apache.maven.plugin.logging.Log;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.rafaelfiume.db.plugin.sqlscripts.Script.newScript;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static support.Decorators.to;
import static support.Decorators.version;

@RunWith(MockitoJUnitRunner.class)
public class DbIncrementorTest {

    private static final String PETITIONS_SCHEMA = "petitions";

    @Mock private SimpleJdbcDatabaseSupport db;
    @Mock private Log log;
    @Mock private VersionBase versionBase;

    private DbIncrementor subject;
    private Version updateToVersion;

    @Test
    public void updateDatabaseFromVersion_i01_01_ToVersion_i02_01() {
        given_current   (version("i01", "01"));
        given_updatingTo(version("i02", "01"));
        givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir();

        // when...
        subject.incrementDb(PETITIONS_SCHEMA, to(version("i02", "01")));

        // then the first script i01.01 shouldn't be executed since it has already been in the past
        then(db).should(never()).execute(scriptFrom("scripts/i01/01.create-a-table-here.sql"));
        then(versionBase).should(never()).updateVersionTo(version("i01", "01"));

        // then other scripts should be executed till db reaches the desired major/minor version
        then(db).should(times(1)).execute(scriptFrom("scripts/i01/02.doing-something-script.sql"));
        then(versionBase).should().updateVersionTo(version("i01", "02"));

        then(db).should(times(1)).execute(scriptFrom("scripts/i01/03.doing-something-else-script.sql"));
        then(versionBase).should().updateVersionTo(version("i01", "03"));

        then(db).should(times(1)).execute(scriptFrom("scripts/i02/01.create-another-table-in-another-iteration.sql"));
        then(versionBase).should().updateVersionTo(version("i02", "01"));
    }

    private void given_current(Version version) {
        given(versionBase.currentVersion()).willReturn(version);
    }

    private void given_updatingTo(Version version) {
        updateToVersion = version;
    }

    private void givenDbIncrementorIsSetUpToExecuteScriptsInThe_scripts_Dir() {
        this.subject = new DbIncrementor(db, versionBase, updateToVersion, log);
    }

    // Decorators

    private String scriptFrom(String name) {
        return newScript(name).content();
    }

}
