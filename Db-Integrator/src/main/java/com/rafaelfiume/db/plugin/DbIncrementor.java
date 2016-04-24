package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.sqlscripts.ScriptFilesNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import org.apache.maven.plugin.logging.Log;

public class DbIncrementor {
    public DbIncrementor(SimpleJdbcDatabaseSupport dbSupport, ScriptsNavigator scriptsNavigator, ScriptsReader scriptsReader, Log log) {
    }

    public void incrementDb(String petitionsSchema) {
    }
}
