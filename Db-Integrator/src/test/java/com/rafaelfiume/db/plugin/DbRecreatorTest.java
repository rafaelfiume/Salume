package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptsNavigator;
import com.rafaelfiume.db.plugin.support.ScriptsReader;
import com.rafaelfiume.db.plugin.support.SimpleDatabaseSupport;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DbRecreatorTest {

    public static final String DATABASE_URL = "postgres://username:password@address:9999/dbname";
    public static final String MOVIESTORE_SCHEMA = "moviestore";
    public static final String A_SQL_SCRIPT = "some SQL here";

    @Mock
    private SimpleDatabaseSupport dbSupport;

    @Mock
    private ScriptsReader scriptsReader;

    @Mock
    private Log log;

    @Mock
    private ScriptsNavigator scriptsNavigator;

    private DbRecreator dbRecreator;

    @Before
    public void setUp() {
        dbRecreator = new DbRecreator(log, scriptsNavigator, scriptsReader);
    }

    @Test
    public void shouldRecreateDatabase() {
        // pre act
        when(scriptsNavigator.hasNext()).thenReturn(true).thenReturn(false);
        when(scriptsNavigator.next()).thenReturn("scripts/i01/some-awkward-script.sql");
        when(scriptsReader.readScript("scripts/i01/some-awkward-script.sql")).thenReturn(A_SQL_SCRIPT);

        // act
        dbRecreator.recreateDb(DATABASE_URL, MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(dbSupport, times(1)).dropAndCreate(MOVIESTORE_SCHEMA);
        verify(dbSupport, times(1)).execute(A_SQL_SCRIPT);
    }

    @Test
    public void skipDroppingDatabaseSchemaButTryToExecuteScriptsAnyway() {
        // pre act

        when(scriptsNavigator.hasNext()).thenReturn(true).thenReturn(false);
        when(scriptsNavigator.next()).thenReturn("scripts/i01/some-awkward-script.sql");
        when(scriptsReader.readScript("scripts/i01/some-awkward-script.sql")).thenReturn(A_SQL_SCRIPT);

        final RuntimeException toBeThrown = new RuntimeException();
        doThrow(toBeThrown).when(dbSupport).dropAndCreate(MOVIESTORE_SCHEMA);

        // act
        dbRecreator.recreateDb(DATABASE_URL, MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(log).warn("Recreating db now...");
        verify(log).warn("Database URL is: ############");

        verify(dbSupport, times(1)).dropAndCreate(MOVIESTORE_SCHEMA);
        verify(log).warn("Failed to drop schema " + MOVIESTORE_SCHEMA + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", toBeThrown);
        verify(dbSupport, times(1)).execute(A_SQL_SCRIPT);
    }

    //
    //// Sad path
    //

    @Test
    public void shouldInterruptDbRecreationWhenDbUrlIsEmpty() {
        // act
        dbRecreator.recreateDb("", MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(log).warn("Skipping db recreation because databaseUrl was not set");
        verify(dbSupport, never()).dropAndCreate(any());
        verify(dbSupport, never()).execute(any());
    }

}
