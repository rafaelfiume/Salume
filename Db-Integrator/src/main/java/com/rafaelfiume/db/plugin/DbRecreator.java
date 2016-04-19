package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptsNavigator;
import com.rafaelfiume.db.plugin.support.ScriptsReader;
import com.rafaelfiume.db.plugin.support.SimpleJdbcDatabaseSupport;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

public class DbRecreator {

    private final SimpleJdbcDatabaseSupport dbSupport;
    private final ScriptsNavigator scriptsNavigator;
    private final ScriptsReader scriptsReader;
    private final Log log;

    public DbRecreator(SimpleJdbcDatabaseSupport dbSupport, ScriptsNavigator scriptsNavigator, ScriptsReader scriptsReader, Log log) {
        this.dbSupport = dbSupport;
        this.scriptsNavigator = scriptsNavigator;
        this.scriptsReader = scriptsReader;
        this.log = log;
    }

    public void recreateDb(String schema) {
        dropDatabaseIfAlreadyExists(schema);
        loadSqlScriptsAndExecuteThem();
    }

    private void dropDatabaseIfAlreadyExists(String schema) {
        log.info("First, dropping schema " + schema + "...");
        try {
            dbSupport.dropAndCreate(schema);
        } catch (Exception e) {
            log.warn("Failed to drop schema " + schema + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", e);
        }
        log.info("Done dropping schema " + schema + "...");
    }

    private void loadSqlScriptsAndExecuteThem() {
        log.info("Second, loading statements...");

        while (scriptsNavigator.hasNext()){
            executeScripts(scriptsNavigator.next());
        }
        IOUtils.closeQuietly(scriptsNavigator);
    }

    private void executeScripts(String scriptFile) {
        final String script = scriptsReader.readScript(scriptFile);

        log.info("Executing script: " + scriptFile);
        log.debug("Script is: " + script);
        try {
            dbSupport.execute(script);
        } catch (Exception e) {
            log.error("Failed to execute statements", e);
            throw e;
        }
    }
}
