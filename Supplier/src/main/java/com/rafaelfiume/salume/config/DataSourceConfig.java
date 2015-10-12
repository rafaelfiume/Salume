package com.rafaelfiume.salume.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.System.getenv;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@Configuration
public class DataSourceConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DataSourceConfig.class);

    private static final String DATABASE_URL = "DATABASE_URL";

    // TODO RF 10/10/2015 Replace Commons Dbcp by a more robust one (HikariCP? Tomcat DataSource (tomcat-jdbc)?)

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        Validate.notNull(getenv(DATABASE_URL), "missing %s environment variable", DATABASE_URL);

        final URI dbUri = new URI(getenv(DATABASE_URL));

        final StringBuilder dbUriBuilder = new StringBuilder("jdbc:postgresql://")
                .append(dbUri.getHost())
                .append(':').append(dbUri.getPort())
                .append(dbUri.getPath());

        final String query = dbUri.getQuery();
        if (isNoneEmpty(query)) {
            dbUriBuilder.append("?").append(query);
        }

        LOG.info("dbUrl: " + dbUriBuilder.toString()); // This ugly thing will expose the dev db url in Travis logs. Delete the log one the build completes...

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUriBuilder.toString());
        basicDataSource.setUsername(dbUri.getUserInfo().split(":")[0]);
        basicDataSource.setPassword(dbUri.getUserInfo().split(":")[1]);

        return basicDataSource;
    }

}
