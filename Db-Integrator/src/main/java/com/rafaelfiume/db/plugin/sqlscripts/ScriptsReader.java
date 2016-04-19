package com.rafaelfiume.db.plugin.sqlscripts;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class ScriptsReader {

    public String readScript(String source) {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream(source)) {
            return IOUtils.toString(is);

        } catch (Exception e) {
            throw new RuntimeException(String.format("could not load scripts from %s", source), e);
        }
    }

}
