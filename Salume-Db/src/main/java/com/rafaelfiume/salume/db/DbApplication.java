package com.rafaelfiume.salume.db;

import com.rafaelfiume.salume.db.config.DataSourceConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@Configuration
@Import({
        DataSourceConfig.class
})
public class DbApplication {
}
