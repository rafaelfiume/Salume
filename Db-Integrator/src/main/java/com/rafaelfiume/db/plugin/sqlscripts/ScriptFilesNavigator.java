package com.rafaelfiume.db.plugin.sqlscripts;

import com.rafaelfiume.db.plugin.Version;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Iterator;
import java.util.stream.Stream;

import static com.rafaelfiume.db.plugin.sqlscripts.Script.newScript;
import static java.nio.file.FileSystems.newFileSystem;
import static org.apache.commons.io.IOUtils.closeQuietly;

/**
 * Not reusable navigator: instantiate a new one every time navigating through scripts dir is needed.
 */
public class ScriptFilesNavigator implements ScriptsNavigator {

    private static final String SCRIPTS_DIR = "scripts";

    private final Iterator<Script> iterator;
    private FileSystem fileSystem;

    public ScriptFilesNavigator() {
        this.iterator = scriptsDirPath()
                .map(path -> newScript("scripts" + path.toString().split(SCRIPTS_DIR)[1]))
                .sequential()
                .iterator();
    }

    public ScriptFilesNavigator(Version from, Version to) {
        this.iterator = scriptsDirPath()
                .map(path -> newScript("scripts" + path.toString().split(SCRIPTS_DIR)[1]))
                .filter(script -> script.isBetween(from, to))
                .sequential()
                .iterator();
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public Script next() {
        return iterator.next();
    }

    private Stream<Path> scriptsDirPath() {
        try {
            final URI uri = ScriptFilesNavigator.class.getResource("/" + SCRIPTS_DIR).toURI();
            final Path scriptsPath;
            if (uri.getScheme().equals("jar")) {
                this.fileSystem = newFileSystem(uri, Collections.<String, Object>emptyMap());
                scriptsPath = fileSystem.getPath("/" + SCRIPTS_DIR);
            } else {
                scriptsPath = Paths.get(uri);
            }

            return Files.find(scriptsPath, 2,
                    (path, basicFileAttributes) -> basicFileAttributes.isRegularFile()
            ).sorted();

        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("failed to find scripts dir", e);
        }
    }

    @Override
    public void close() {
        closeQuietly(fileSystem);
    }
}
