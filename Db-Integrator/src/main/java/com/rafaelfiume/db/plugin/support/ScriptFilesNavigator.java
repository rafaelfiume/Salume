package com.rafaelfiume.db.plugin.support;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Iterator;

/**
 * Not reusable navigator: instantiate a new one every time navigating through scripts dir is needed.
 */
public class ScriptFilesNavigator {

    private final Collection<File> files;
    private final Iterator<File> fileIterator;

    public ScriptFilesNavigator() throws URISyntaxException {
        final URI uri = ScriptFilesNavigator.class.getResource("/scripts").toURI();
        final File scriptsFolder = new File(uri);

        this.files = FileUtils.listFiles(scriptsFolder, new String[]{"sql"}, true);
        this.fileIterator = FileUtils.iterateFiles(scriptsFolder, new String[]{"sql"}, true);
    }

    public int numberOfFiles() {
        return files.size();
    }

    public boolean hasNext() {
        return fileIterator.hasNext();
    }

    public String next() {
        final File scriptFile = fileIterator.next();
        return "scripts/" + scriptFile.getParentFile().getName() + "/" + scriptFile.getName();
    }

}
