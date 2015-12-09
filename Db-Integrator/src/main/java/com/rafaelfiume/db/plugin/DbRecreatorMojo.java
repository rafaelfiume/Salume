package com.rafaelfiume.db.plugin;

import com.rafaelfiume.db.plugin.config.DataSourceConfig;
import com.rafaelfiume.db.plugin.support.ScriptsSource;
import com.rafaelfiume.db.plugin.support.SimpleJdbcDatabaseSupport;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import static org.apache.maven.plugins.annotations.LifecyclePhase.TEST_COMPILE;

/**
 * This a very simplistic version of a plugin to update the database in dev, staging and prod environments.
 * It will be improved as the stories are played. \n\n
 * <p/>
 * It should change the db according with the scripts it finds in the scripts directory.
 */
@Mojo(name = "recreatedb", threadSafe = false, aggregator = true, defaultPhase = TEST_COMPILE)
public class DbRecreatorMojo extends AbstractMojo {

    // TODO RF 12/10/2015 pass this as a maven property e.g. ${db.schema.name}
    private static final String SCHEMA = "salumistore";

    private final DbRecreator dbRecreator;

    @Parameter(property = "databaseUrl", readonly = true, required = false)
    private String databaseUrl;

    public DbRecreatorMojo() {
        this.dbRecreator = new DbRecreator(getLog(), new ScriptsSource());
    }

    public void execute() throws MojoExecutionException {
        dbRecreator.recreateDb(
                databaseUrl,
                SCHEMA,
                new SimpleJdbcDatabaseSupport(new DataSourceConfig().dataSource(databaseUrl))
        );
    }

}
