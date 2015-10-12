package com.rafaelfiume.db.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class DataSourceConfig {

    private static final String DATABASE_URL = "DATABASE_URL";

    // TODO RF 10/10/2015 Triplicated from Supplier module

    public DataSource dataSource() throws URISyntaxException {
        Validate.notNull(getenv(DATABASE_URL), "missing %s environment variable", DATABASE_URL);

        final URI dbUri = new URI(getenv(DATABASE_URL));

        final StringBuilder dbUriBuilder = new StringBuilder("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':').append(dbUri.getPort())
                .append(dbUri.getPath());

        final String query = dbUri.getQuery();
        if (isNotEmpty(query)) {
            dbUriBuilder.append("?").append(query);
        }

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUriBuilder.toString());
        basicDataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
        basicDataSource.setPassword(dbUri.getUserInfo().split(":")[1]);

        return basicDataSource;
    }

}
