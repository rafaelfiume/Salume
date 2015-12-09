package com.rafaelfiume.db.plugin.support;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertNotNull;

public class ScriptsReaderTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void retrieveScriptFile() {
        final String scripts = new ScriptsReader().getScripts("scripts/i01/01.create-table.sql");
        assertNotNull(scripts);
    }

    @Test
    public void throwIrrecoverableExceptionWhenCanNotFindDbScripts() {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("could not load scripts from scripts/inexistent.script.sql");
        new ScriptsReader().getScripts("scripts/inexistent.script.sql");
    }

}
