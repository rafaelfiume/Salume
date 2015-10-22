package com.rafaelfiume.salume.db.support;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.junit.Assert.assertNotNull;

public class ScriptsSourceTest {

    @Test
    public void retrieveScriptFile() throws IOException, URISyntaxException {
        final String scripts = new ScriptsSource().getScripts();
        assertNotNull(scripts);
    }

}
