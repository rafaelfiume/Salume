package com.rafaelfiume.db;

import com.rafaelfiume.db.config.DataSourceConfig;
import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.apache.commons.io.FileUtils.readFileToString;
import static org.apache.maven.plugins.annotations.LifecyclePhase.VERIFY;

/**
 * This a very simplistic version of a plugin to update the database in dev, staging and prod environments.
 * It will be improved as the stories are played. \n\n
 * <p/>
 * It should change the db according with the scripts it finds in the scripts directory.
 */
@Mojo(name = "recreatedb", aggregator = true, defaultPhase = VERIFY)
public class DbRecreator extends AbstractMojo {

    // TODO RF 12/10/2015 pass this as a maven property e.g. ${db.schema.name}
    private static final String SCHEMA = "salumistore";

    private final SimpleDatabaseSupport dbSupport;

    @Parameter(property = "sql.scripts.dir", required = true)
    private File sqlScript;

    @Autowired
    public DbRecreator() throws URISyntaxException {
        this.dbSupport = new SimpleDatabaseSupport(new DataSourceConfig().dataSource());
    }

    public void execute() throws MojoExecutionException {
        getLog().warn("Recreating db now... ");

        getLog().info("First, dropping schema " + SCHEMA);
        dbSupport.dropDb(SCHEMA);

        getLog().info("Second, loading statement from " + sqlScript);
        final String script;
        try {
            script = getScript();
        } catch (IOException e) {
            getLog().error("failed to load sql scripts from scripts dir");
            throw new RuntimeException(e);
        }
        getLog().info("Script is: " + script);
        dbSupport.execute(script);

        getLog().warn("Done recreating db :-)");
    }

    public String getScript() throws IOException {
        return readFileToString(sqlScript);
    }
}
