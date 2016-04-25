package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static support.Decorators.script;

@RunWith(MockitoJUnitRunner.class)
public class DbRecreatorTest {

    private static final String BOOKING_SCHEMA = "booking_schema";
    private static final String A_SQL_SCRIPT = "some SQL here";
    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException();

    @Mock private SimpleJdbcDatabaseSupport db;
    @Mock private ScriptsReader scriptsReader;
    @Mock private Log log;
    @Mock private ScriptsNavigator scriptsNavigator;

    private DbRecreator subject;

    @Before
    public void setUp() {
        subject = new DbRecreator(db, scriptsNavigator, scriptsReader, log);
    }

    @Test
    public void shouldRecreateDatabase() {
        given(scriptsNavigator.hasNext()).willReturn(true, false);
        given(scriptsNavigator.next()).willReturn(script("scripts/i01/01/a-script.sql"));
        given(scriptsReader.read(script("scripts/i01/01/a-script.sql"))).willReturn(A_SQL_SCRIPT);

        // when...
        subject.recreateDb(BOOKING_SCHEMA);

        then(db).should(times(1)).dropAndCreate(BOOKING_SCHEMA);
        then(db).should(times(1)).execute(A_SQL_SCRIPT);
    }

    @Test // Note : 16/04/2016 : Maybe this behavior will change ounce DbIncrementor is ready
    public void skipsDroppingDatabaseSchemaWhenItFailsButTryToExecuteScriptsAnyway() {
        given(scriptsNavigator.hasNext()).willReturn(true, false);
        given(scriptsNavigator.next()).willReturn(script("scripts/i01/01/a-script.sql"));
        given(scriptsReader.read(script("scripts/i01/01/a-script.sql"))).willReturn(A_SQL_SCRIPT);
        willThrow(RUNTIME_EXCEPTION).given(db).dropAndCreate(BOOKING_SCHEMA);

        // when...
        subject.recreateDb(BOOKING_SCHEMA);

        then(db).should(times(1)).dropAndCreate(BOOKING_SCHEMA);
        then(log).should().warn("Failed to drop schema " + BOOKING_SCHEMA + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", RUNTIME_EXCEPTION);
        then(db).should(times(1)).execute(A_SQL_SCRIPT);
    }

}
