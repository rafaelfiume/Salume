package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.config.DataSourceConfig;
import com.rafaelfiume.db.plugin.support.ScriptsSource;
import com.rafaelfiume.db.plugin.support.SimpleDatabaseSupport;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.apache.maven.plugins.annotations.LifecyclePhase.TEST_COMPILE;

/**
 * This a very simplistic version of a plugin to update the database in dev, staging and prod environments.
 * It will be improved as the stories are played. \n\n
 * <p/>
 * It should change the db according with the scripts it finds in the scripts directory.
 */
@Mojo(name = "recreatedb", threadSafe = false, aggregator = true, defaultPhase = TEST_COMPILE)
public class DbRecreator extends AbstractMojo {

    // TODO RF 12/10/2015 pass this as a maven property e.g. ${db.schema.name}
    private static final String SCHEMA = "salumistore";

    private final ScriptsSource scriptsSource;

    @Parameter(property = "databaseUrl", readonly = true, required = false)
    private String databaseUrl;

    public DbRecreator() {
        this.scriptsSource = new ScriptsSource();
    }

    public void execute() throws MojoExecutionException {
        if (StringUtils.isEmpty(databaseUrl)) {
            getLog().warn("Skipping db recreation because databaseUrl was not set");
            return;
        } else {
            getLog().warn("Recreating db now...");
            getLog().warn("Database URL is: ############"); // Can't let the URL to appear in the logs.
        }

        final SimpleDatabaseSupport dbSupport;
        try {
            dbSupport = new SimpleDatabaseSupport(new DataSourceConfig().dataSource(databaseUrl));
        } catch (URISyntaxException e) {
            getLog().error("failed to load sql scripts from scripts dir");
            throw new RuntimeException(e);
        }

        getLog().info("First, dropping schema " + SCHEMA + "...");
        try {
            dbSupport.dropDb(SCHEMA);
        } catch (Exception e) {
            getLog().warn("Failed to drop schema " + SCHEMA + ". (Maybe the schema was ever created?) Trying to proceed with db recreation anyway...", e);
        }
        getLog().info("Done dropping schema " + SCHEMA + "...");

        getLog().info("Second, loading statements...");
        final String script;
        try {
            script = scriptsSource.getScripts();
        } catch (IOException | URISyntaxException e) {
            getLog().error("failed to load sql scripts from scripts dir");
            throw new RuntimeException(e);
        }
        getLog().info("Script is: " + script);
        try {
            dbSupport.execute(script);
        } catch (Exception e) {
            getLog().warn("Failed to execute statements", e);
            throw e;
        }

        getLog().warn("Done recreating db :-)");
    }

}
