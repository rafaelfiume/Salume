package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptsSource;
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

    @Mock
    private SimpleDatabaseSupport dbSupport;

    @Mock
    private ScriptsSource scriptsSource;

    @Mock
    private Log log;

    private DbRecreator dbRecreator;

    @Before
    public void setUp() {
        dbRecreator = new DbRecreator(log, scriptsSource);
    }

    @Test
    public void shouldRecreateDatabase() {
        // pre act
        when(scriptsSource.getScripts(any())).thenReturn("some nice sql here");

        // act
        dbRecreator.recreateDb(DATABASE_URL, MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(dbSupport, times(1)).dropDb(MOVIESTORE_SCHEMA);
        verify(dbSupport, times(1)).execute("some nice sql here");
    }

    @Test
    public void shouldInterruptDbRecreationWhenDbUrlIsEmpty() {
        // act
        dbRecreator.recreateDb("", MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(log).warn("Skipping db recreation because databaseUrl was not set");
        verify(dbSupport, never()).dropDb(any());
        verify(dbSupport, never()).execute(any());
    }

    @Test
    public void skipDroppingDatabaseSchemaButTryToExecuteScriptsAnyway() {
        // pre act
        final RuntimeException toBeThrown = new RuntimeException();
        doThrow(toBeThrown).when(dbSupport).dropDb(MOVIESTORE_SCHEMA);
        when(scriptsSource.getScripts(any())).thenReturn("some nice sql here");

        // act
        dbRecreator.recreateDb(DATABASE_URL, MOVIESTORE_SCHEMA, dbSupport);

        // post act
        verify(log).warn("Recreating db now...");
        verify(log).warn("Database URL is: ############");

        verify(dbSupport, times(1)).dropDb(MOVIESTORE_SCHEMA);
        verify(log).warn("Failed to drop schema " + MOVIESTORE_SCHEMA + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", toBeThrown);
        verify(dbSupport, times(1)).execute("some nice sql here");
    }

}
