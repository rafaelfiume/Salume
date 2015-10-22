package com.rafaelfiume.salume.db.support;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ScriptsSource {

    public String getScripts() throws IOException, URISyntaxException {
        final InputStream is = getClass().getClassLoader().getResourceAsStream("scripts/01.create-table.sql");
        return IOUtils.toString(is);
    }

}
