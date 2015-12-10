package com.rafaelfiume.db.plugin.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;

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
            throw new RuntimeException("irrecoverable exception: wrong database url", e);
        }

        final StringBuilder dbUriBuilder = new StringBuilder("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':').append(dbUri.getPort())
                .append(dbUri.getPath());

        final String query = dbUri.getQuery();
        if (isNoneEmpty(query)) {
            dbUriBuilder.append("?").append(query);
        }

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUriBuilder.toString());
        config.setUsername(dbUri.getUserInfo().split(":")[0]);
        config.setPassword(dbUri.getUserInfo().split(":")[1]);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
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
