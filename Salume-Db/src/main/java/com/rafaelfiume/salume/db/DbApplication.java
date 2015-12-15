package com.rafaelfiume.salume.db;

import com.rafaelfiume.salume.db.config.DataSourceConfig;
import com.rafaelfiume.salume.db.config.DomainConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@Configuration
@Import({
        DataSourceConfig.class,
        DomainConfig.class
})
public class DbApplication {
}
