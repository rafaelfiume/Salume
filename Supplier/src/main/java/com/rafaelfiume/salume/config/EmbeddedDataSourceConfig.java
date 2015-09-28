package com.rafaelfiume.salume.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2;

// TODO RF 28/09/15 Create an embedded profile so running in the dev mode will also be possible using PostgreSQL for instance
@Profile("dev")
@Configuration
public class EmbeddedDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        return new EmbeddedDatabaseBuilder()
                .generateUniqueName(true)
                .setType(H2)
                .setScriptEncoding("UTF-8")
                .ignoreFailedDrops(true)
//              .addScript("schema.sql")
//              .addScripts("user_data.sql", "country_data.sql") // coming soon
                .build();
    }
}
