package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.database.VersionBase;
import com.rafaelfiume.db.plugin.sqlscripts.Script;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptFilesNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsNavigator;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptsReader;
import org.apache.maven.plugin.logging.Log;

public class DbIncrementor {

    private final SimpleJdbcDatabaseSupport db;
    private final VersionBase versionBase;
    private final Version updateToVersion;
    private final ScriptsReader scriptsReader;
    private final Log log;

    public DbIncrementor(SimpleJdbcDatabaseSupport db, VersionBase versionBase, Version updateToVersion, ScriptsReader scriptsReader, Log log) {
        this.db = db;
        this.versionBase = versionBase;
        this.updateToVersion = updateToVersion;
        this.scriptsReader = scriptsReader;
        this.log = log;
    }

    public void incrementDb(String schema, Version to) {
        final Version from = versionBase.currentVersion();

        final ScriptsNavigator scriptsNavigator = new ScriptFilesNavigator(from, to);
        while (scriptsNavigator.hasNext()){
            executeScripts(scriptsNavigator.next());
            //versionBase.updateMajorVersionTo(scriptsNavigator.nextVersion());
        }
        scriptsNavigator.close();
    }

    // TODO RF 25/04/2016 Duplicated
    private void executeScripts(Script script) {
        final String sql = scriptsReader.read(script);

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
