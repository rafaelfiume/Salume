package com.rafaelfiume.salume.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.Validate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Configuration
public class DataSourceConfig {

    private static final String DATABASE_URL = "DATABASE_URL";
    
    @Bean
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

        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUriBuilder.toString());
        config.setUsername(dbUri.getUserInfo().split(":")[0]);
        config.setPassword(dbUri.getUserInfo().split(":")[1]);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        return new HikariDataSource(config);
    }

}
