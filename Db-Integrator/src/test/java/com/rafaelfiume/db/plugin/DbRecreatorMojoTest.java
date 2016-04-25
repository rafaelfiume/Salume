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

    private DbRecreatorMojo subject;

    @Before
    public void setUp() {
        this.subject = new DbRecreatorMojo(dbRecreator, log);
    }

    ///////////////////////////// Sad path... ///////////////////////////////

    @Test
    public void skipsDbRecreationWhenDatabaseUrlIsEmpty() throws MojoExecutionException, NoSuchFieldException, IllegalAccessException {
        givenDatabaseUrlIsEmpty();

        // when...
        subject.execute();

        then(dbRecreator).should(never()).recreateDb(any());
    }

    @Test
    public void logsWhenDatabaseRecreationIsSkipped() throws MojoExecutionException, NoSuchFieldException, IllegalAccessException {
        givenDatabaseUrlIsEmpty();

        // when...
        subject.execute();

        then(log).should().warn("Recreating db now...");
        then(log).should().warn("Database URL is: ############");
        then(log).should().warn("Skipping db recreation because databaseUrl was not set");
    }

    private void givenDatabaseUrlIsEmpty() throws NoSuchFieldException, IllegalAccessException {
        Field databaseUrlField = subject.getClass().getDeclaredField("databaseUrl");
        databaseUrlField.setAccessible(true);
        databaseUrlField.set(subject, "");
    }
}
