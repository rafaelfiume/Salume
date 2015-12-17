package com.rafaelfiume.db.plugin.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

public class DataSourceConfig {

    // TODO RF 23/10/2015 Duplicated from Salume-Db

    // Used by Db-Integrator plugin
    public DataSource dataSource(String databaseUrl) {
        if (isBlank(databaseUrl)) return new EmptyDataSource(); // let upper classes deal with missing db url

        final URI dbUri;
        try {
            dbUri = new URI(databaseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(format("error creating URI from %s", databaseUrl), e);
        }

        final StringBuilder dbUriBuilder = new StringBuilder("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':').append(dbUri.getPort())
                .append(dbUri.getPath());

        final String query = dbUri.getQuery();
        if (isNoneEmpty(query)) {
            dbUriBuilder.append("?").append(query);
        }

        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUriBuilder.toString());
        basicDataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
        basicDataSource.setPassword(dbUri.getUserInfo().split(":")[1]);
        basicDataSource.setMaxTotal(1);

        return basicDataSource;
    }

    static class EmptyDataSource extends AbstractDataSource {

        @Override
        public Connection getConnection(String username, String password) throws SQLException {
            return getConnection();
        }

        @Override
        public Connection getConnection() throws SQLException {
            throw new UnsupportedOperationException("operation not supported: database url was not set");
        }
    }

}
