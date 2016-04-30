package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.Schema;
import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.sqlscripts.Script;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsNavigator;
import org.apache.maven.plugin.logging.Log;

public class DbRecreator {

    private final SimpleJdbcDatabaseSupport db;
    private final ScriptsNavigator scriptsNavigator;
    private final Log log;

    public DbRecreator(SimpleJdbcDatabaseSupport db, ScriptsNavigator scriptsNavigator, Log log) {
        this.db = db;
        this.scriptsNavigator = scriptsNavigator;
        this.log = log;
    }

    public void recreateDb(Schema schema) {
        dropDatabaseIfAlreadyExists(schema);
        loadSqlScriptsAndExecuteThem();
    }

    private void dropDatabaseIfAlreadyExists(Schema schema) {
        log.info("First, dropping schema " + schema + "...");
        try {
            db.dropAndCreate(schema);
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
        scriptsNavigator.close();
    }

    private void executeScripts(Script script) {
        final String sql = script.content();

        log.info("Executing script: " + script);
        log.debug("Script is: " + sql);
        try {
            db.execute(sql);
        } catch (Exception e) {
            log.error("Failed to execute statements", e);
            throw e;
        }
    }
}
