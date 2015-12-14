package com.rafaelfiume.salume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;

@Order(LOWEST_PRECEDENCE)
@Configuration
public class MisconfiguredDataSourceConfig {

    @SuppressWarnings("unused")
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource unknownDataSource = new DriverManagerDataSource(
                "jdbc:postgresql://localhost:5432/wrongdb",
                "postgres",
                "######"
        );
        return unknownDataSource;
    }

}
