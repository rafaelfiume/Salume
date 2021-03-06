package com.rafaelfiume.salume.db.config;

import com.rafaelfiume.salume.domain.FatConverter;
import com.rafaelfiume.salume.domain.MoneyDealer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public FatConverter fatConverter() {
        return new FatConverter();
    }

    @Bean
    public MoneyDealer moneyDealer() {
        return new MoneyDealer();
    }
}
