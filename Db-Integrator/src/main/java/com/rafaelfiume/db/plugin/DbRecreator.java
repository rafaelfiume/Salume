package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.support.ScriptsSource;
import com.rafaelfiume.db.plugin.support.SimpleDatabaseSupport;
import org.apache.maven.plugin.logging.Log;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class DbRecreator {

    private final Log log;
    private final ScriptsSource scriptsSource;

    public DbRecreator(Log log, ScriptsSource scriptsSource) {
        this.log = log;
        this.scriptsSource = scriptsSource;
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
        final String script = scriptsSource.getScripts("scripts/01.create-table.sql");

        log.info("Script is: " + script);
        try {
            dbSupport.execute(script);
        } catch (Exception e) {
            log.warn("Failed to execute statements", e);
            throw e;
        }
    }
}
