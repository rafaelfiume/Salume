package com.rafaelfiume.salume.db.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static java.lang.System.getenv;

@Configuration
@EnableTransactionManagement
public class DataSourceConfig {

    public static final String SPRING_DATASOURCE_URL = "SPRING_DATASOURCE_URL";
    public static final String SPRING_DATASOURCE_USERNAME = "SPRING_DATASOURCE_USERNAME";
    public static final String SPRING_DATASOURCE_PASSWORD = "SPRING_DATASOURCE_PASSWORD";

    @Bean
    public DataSource dataSource() {
        return dataSource(
                getenv(SPRING_DATASOURCE_URL),
                getenv(SPRING_DATASOURCE_USERNAME),
                getenv(SPRING_DATASOURCE_PASSWORD)
        );
    }

    @Bean
    public PlatformTransactionManager txManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    // Used by Db-Integrator plugin
    public DataSource dataSource(String url, String user, String pass) {
        final HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(pass);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setMaximumPoolSize(5);

        return new HikariDataSource(config);
    }

}
