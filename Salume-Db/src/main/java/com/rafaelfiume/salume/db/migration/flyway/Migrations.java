package com.rafaelfiume.salume.db.migration.flyway;

import org.flywaydb.core.Flyway;

import static com.rafaelfiume.salume.db.config.DataSourceConfig.SPRING_DATASOURCE_PASSWORD;
import static com.rafaelfiume.salume.db.config.DataSourceConfig.SPRING_DATASOURCE_URL;
import static com.rafaelfiume.salume.db.config.DataSourceConfig.SPRING_DATASOURCE_USERNAME;
import static java.lang.System.getenv;
import static org.flywaydb.core.api.MigrationVersion.fromVersion;

public class Migrations {

    public static void main(String[] args) throws Exception {
        Flyway flyway = new Flyway();
        flyway.setDataSource(
                getenv(SPRING_DATASOURCE_URL),
                getenv(SPRING_DATASOURCE_USERNAME),
                getenv(SPRING_DATASOURCE_PASSWORD));
        flyway.setSchemas("salumistore");
        flyway.setBaselineOnMigrate(true);
        flyway.setBaselineVersion(fromVersion("3"));
        flyway.setBaselineDescription("Baseline version: 3");
        flyway.migrate();
    }

}
