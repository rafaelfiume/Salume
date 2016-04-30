package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.Schema;
import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.sqlscripts.Script;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsNavigator;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static com.rafaelfiume.db.plugin.database.Schema.schema;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

@RunWith(MockitoJUnitRunner.class)
public class DbRecreatorTest {

    private static final Schema BOOKING_SCHEMA = schema("booking_schema");
    private static final String A_SQL_SCRIPT = "some SQL here";
    private static final RuntimeException RUNTIME_EXCEPTION = new RuntimeException();

    @Mock private SimpleJdbcDatabaseSupport db;
    @Mock private Log log;
    @Mock private ScriptsNavigator scriptsNavigator;
    @Mock private Script aSqlScript;

    private DbRecreator subject;

    @Before
    public void setUp() {
        subject = new DbRecreator(db, scriptsNavigator, log);
    }

    @Test
    public void shouldRecreateDatabase() {
        given(scriptsNavigator.hasNext()).willReturn(true, false);
        given(scriptsNavigator.next()).willReturn(aSqlScript);
        given(aSqlScript.content()).willReturn(A_SQL_SCRIPT);

        // when...
        subject.recreateDb(BOOKING_SCHEMA);

        then(db).should(times(1)).dropAndCreate(BOOKING_SCHEMA);
        then(db).should(times(1)).execute(A_SQL_SCRIPT);
    }

    @Test // Note : 16/04/2016 : Maybe this behavior will change ounce DbMigration is ready
    public void skipsDroppingDatabaseSchemaWhenItFailsButTryToExecuteScriptsAnyway() {
        willThrow(RUNTIME_EXCEPTION).given(db).dropAndCreate(BOOKING_SCHEMA);
        given(scriptsNavigator.hasNext()).willReturn(true, false);
        given(scriptsNavigator.next()).willReturn(aSqlScript);
        given(aSqlScript.content()).willReturn(A_SQL_SCRIPT);

        // when...
        subject.recreateDb(BOOKING_SCHEMA);

        then(db).should(times(1)).dropAndCreate(BOOKING_SCHEMA);
        then(log).should().warn("Failed to drop schema " + BOOKING_SCHEMA + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", RUNTIME_EXCEPTION);
        then(db).should(times(1)).execute(A_SQL_SCRIPT);
    }

}
