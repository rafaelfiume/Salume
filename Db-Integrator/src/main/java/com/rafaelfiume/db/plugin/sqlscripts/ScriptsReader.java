package com.rafaelfiume.db.plugin.sqlscripts;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class ScriptsReader {

    // TODO RF 25/04/2016 Move this logic inside Script class?
    public String read(Script source) {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream(source.name())) {
            return IOUtils.toString(is);

        } catch (Exception e) {
            throw new RuntimeException(String.format("could not load scripts from: %s", source), e);
        }
    }

}
