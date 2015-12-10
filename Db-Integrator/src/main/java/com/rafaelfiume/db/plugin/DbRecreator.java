package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptsNavigator;
import com.rafaelfiume.db.plugin.support.ScriptsReader;
import com.rafaelfiume.db.plugin.support.SimpleDatabaseSupport;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.logging.Log;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class DbRecreator {

    private final Log log;
    private final ScriptsNavigator scriptsNavigator;
    private final ScriptsReader scriptsReader;

    public DbRecreator(Log log, ScriptsNavigator scriptsNavigator, ScriptsReader scriptsReader) {
        this.log = log;
        this.scriptsNavigator = scriptsNavigator;
        this.scriptsReader = scriptsReader;
    }

    public void recreateDb(String databaseUrl, String schema, SimpleDatabaseSupport dbSupport) {
        if (isEmpty(databaseUrl)) {
            log.warn("Skipping db recreation because databaseUrl was not set");
            return;
        }

        log.warn("Recreating db now..."); log.warn("Database URL is: ############"); // Can't let the URL to appear in the logs.

        dropDatabaseIfItAlreadyExists(schema, dbSupport);
        loadSqlScriptsAndExecuteThem(dbSupport);
    }

    private void dropDatabaseIfItAlreadyExists(String schema, SimpleDatabaseSupport dbSupport) {
        log.info("First, dropping schema " + schema + "...");
        try {
            dbSupport.dropDb(schema);
        } catch (Exception e) {
            log.warn("Failed to drop schema " + schema + ". (Maybe the schema was never created?) Trying to proceed with db recreation anyway...", e);
        }
        log.info("Done dropping schema " + schema + "...");
    }

    private void loadSqlScriptsAndExecuteThem(SimpleDatabaseSupport dbSupport) {
        log.info("Second, loading statements...");

        // Very likely missing a colaborator here. Let's see what happens when adding the next functionalities
        while (scriptsNavigator.hasNext()){
            executeScripts(dbSupport, scriptsNavigator.next());
        }
        IOUtils.closeQuietly(scriptsNavigator); // TODO RF 10/12/2015 Fix this
    }

    private void executeScripts(SimpleDatabaseSupport dbSupport, String scriptFile) {
        final String script = scriptsReader.readScript(scriptFile);

        log.info("Executing script: " + scriptFile);
        log.debug("Script is: " + script);
        try {
            dbSupport.execute(script);
        } catch (Exception e) {
            log.warn("Failed to execute statements", e);
            throw e;
        }
    }
}
