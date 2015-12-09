package com.rafaelfiume.db.plugin.support;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class ScriptsReader {

    public String getScripts(String source) {
        try (final InputStream is = getClass().getClassLoader().getResourceAsStream(source)) {
            return IOUtils.toString(is);

        } catch (Exception e) {
            throw new RuntimeException(String.format("could not load scripts from %s", source), e);
        }
    }

}
