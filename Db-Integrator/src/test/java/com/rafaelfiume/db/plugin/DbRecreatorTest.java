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

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DbRecreatorTest {

    private static final String MOVIE_STORE_SCHEMA = "moviestore";
    private static final String A_SQL_SCRIPT = "some SQL here";

    @Mock private SimpleDatabaseSupport dbSupport;
    @Mock private ScriptsReader scriptsReader;
    @Mock private Log log;
    @Mock private ScriptsNavigator scriptsNavigator;

    private DbRecreator underTest;

    @Before
    public void setUp() {
        underTest = new DbRecreator(dbSupport, scriptsNavigator, scriptsReader, log);
    }

    @Test
    public void shouldRecreateDatabase() {
        // pre act
        given(scriptsNavigator.hasNext()).willReturn(true, false);
        given(scriptsNavigator.next()).willReturn("scripts/i01/some-awkward-script.sql");
        given(scriptsReader.readScript("scripts/i01/some-awkward-script.sql")).willReturn(A_SQL_SCRIPT);

        // act
        underTest.recreateDb(MOVIE_STORE_SCHEMA);

        // post act
        verify(dbSupport, times(1)).dropAndCreate(MOVIE_STORE_SCHEMA);
        verify(dbSupport, times(1)).execute(A_SQL_SCRIPT);
    }

    @Test
    public void skipDroppingDatabaseSchemaButTryToExecuteScriptsAnyway() {
        // pre act
        when(scriptsNavigator.hasNext()).thenReturn(true, false);
        when(scriptsNavigator.next()).thenReturn("scripts/i01/some-awkward-script.sql");
        when(scriptsReader.readScript("scripts/i01/some-awkward-script.sql")).thenReturn(A_SQL_SCRIPT);

        final RuntimeException toBeThrown = new RuntimeException();
        doThrow(toBeThrown).when(dbSupport).dropAndCreate(MOVIE_STORE_SCHEMA);

        // act
        underTest.recreateDb(MOVIE_STORE_SCHEMA);

        // post act
        verify(dbSupport, times(1)).dropAndCreate(MOVIE_STORE_SCHEMA);
        verify(log).warn("Failed to drop schema " + MOVIE_STORE_SCHEMA + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", toBeThrown);
        verify(dbSupport, times(1)).execute(A_SQL_SCRIPT);
    }

}
