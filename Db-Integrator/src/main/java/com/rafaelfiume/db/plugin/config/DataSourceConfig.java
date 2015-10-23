package com.rafaelfiume.db.plugin.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

public class DataSourceConfig {

    private static final String DATABASE_URL = "DATABASE_URL";

    // TODO RF 23/10/2015 Duplicated from Salume-Db

    public DataSource dataSource() throws URISyntaxException {
        return dataSource(getenv(DATABASE_URL));
    }

    // Used by Db-Integrator plugin
    public DataSource dataSource(String databaseUrl) throws URISyntaxException {
        Validate.notNull(databaseUrl, "missing %s environment variable", DATABASE_URL);

        final URI dbUri = new URI(databaseUrl);

        final StringBuilder dbUriBuilder = new StringBuilder("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':').append(dbUri.getPort())
                .append(dbUri.getPath());

        final String query = dbUri.getQuery();
        if (isNoneEmpty(query)) {
            dbUriBuilder.append("?").append(query);
        }

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUriBuilder.toString());
        basicDataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
        basicDataSource.setPassword(dbUri.getUserInfo().split(":")[1]);

        return basicDataSource;
    }

}
