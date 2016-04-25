package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.sqlscripts.ScriptFilesNavigator;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import static com.rafaelfiume.db.plugin.database.DataSourceFactory.newDataSource;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.maven.plugins.annotations.LifecyclePhase.TEST_COMPILE;

/**
 * This a very simplistic version of a plugin to update the database in dev, staging and prod environments.
 * It will be improved as the stories are played. \n\n
 * <p/>
 * It should change the db according with the scripts it finds in the scripts directory.
 */
@Mojo(name = "recreatedb", threadSafe = false, aggregator = true, defaultPhase = TEST_COMPILE)
public final class DbRecreatorMojo extends AbstractMojo {

    // TODO RF 12/10/2015 pass this as a maven property e.g. ${db.schema.name}
    private static final String SCHEMA = "salumistore";

    private final Log log;
    private DbRecreator dbRecreator;

    @Parameter(property = "databaseUrl", readonly = true, required = false)
    private String databaseUrl;

    public DbRecreatorMojo() {
        this.log = getLog();
    }

    DbRecreatorMojo(DbRecreator dbRecreator, Log log) {
        this.dbRecreator = dbRecreator;
        this.log = log;
    }

    public void execute() throws MojoExecutionException {
        log.warn("Recreating db now..."); log.warn("Database URL is: ############"); // Can't let the URL to appear in the logs.

        // we need to await till databaseUrl is available, and it is injected by Maven after the constructor is invoked
        initializeDbRecreatorIfThatHasNotBeenDoneYet();

        if (isBlank(databaseUrl)) {
            log.warn("Skipping db recreation because databaseUrl was not set");
            return;
        }
        dbRecreator.recreateDb(SCHEMA);
    }

    private void initializeDbRecreatorIfThatHasNotBeenDoneYet() {
        if (this.dbRecreator != null) { return; }

        this.dbRecreator = new DbRecreator(
                new SimpleJdbcDatabaseSupport(newDataSource(databaseUrl)),
                new ScriptFilesNavigator(),
                this.log
        );
    }

}
