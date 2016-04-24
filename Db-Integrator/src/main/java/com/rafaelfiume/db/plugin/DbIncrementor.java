package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import org.apache.maven.plugin.logging.Log;

public class DbIncrementor {
    public DbIncrementor(SimpleJdbcDatabaseSupport dbSupport, Version updateToVersion, ScriptsReader scriptsReader, Log log) {
    }

    public void incrementDb(String petitionsSchema) {
    }
}
