package com.rafaelfiume.db.plugin;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;

@RunWith(MockitoJUnitRunner.class)
public class DbRecreatorMojoTest {

    @Mock private DbRecreator dbRecreator;
    @Mock private Log log;

    private DbRecreatorMojo underTest;

    @Before
    public void setUp() {
        this.underTest = new DbRecreatorMojo(dbRecreator, log);
    }

    //
    //// Sad path
    //

    @Test
    public void interruptsDbRecreationWhenDbUrlIsEmpty() throws MojoExecutionException, NoSuchFieldException, IllegalAccessException {
        givenDatabaseUrlIsEmpty();

        // when...
        underTest.execute();

        then(log).should().warn("Recreating db now...");
        then(log).should().warn("Database URL is: ############");
        then(log).should().warn("Skipping db recreation because databaseUrl was not set");

        then(dbRecreator).should(never()).recreateDb(any());
    }

    private void givenDatabaseUrlIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        Field databaseUrlField = underTest.getClass().getDeclaredField("databaseUrl");
        databaseUrlField.setAccessible(true);
        databaseUrlField.set(underTest, "");
    }
}
