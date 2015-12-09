package com.rafaelfiume.db.plugin.support;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class ScriptsSourceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void retrieveScriptFile() throws IOException, URISyntaxException {
        final String scripts = new ScriptsSource().getScripts("scripts/01.create-table.sql");
        assertNotNull(scripts);
    }

    @Test
    public void throwIrrecovarableExceptionWhenCanNotFindDbScripts() throws IOException {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("could not load scripts from inexistent.script.sql");
        new ScriptsSource().getScripts("inexistent.script.sql");
    }

    //@Test
    public void retrieveScriptsFromScriptsFolder() {

    }

}
