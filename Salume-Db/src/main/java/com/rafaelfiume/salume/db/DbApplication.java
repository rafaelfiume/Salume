package com.rafaelfiume.salume.db;

import com.rafaelfiume.salume.db.config.DataSourceConfig;
import com.rafaelfiume.salume.db.config.MoneyDealerConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@ComponentScan
@Configuration
@Import({
        DataSourceConfig.class,
        MoneyDealerConfig.class
})
public class DbApplication {
}
