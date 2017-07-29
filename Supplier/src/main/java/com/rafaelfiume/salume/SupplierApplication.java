package com.rafaelfiume.salume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

import static org.springframework.boot.Banner.Mode.OFF;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SupplierApplication {

    public static void main(String... args) throws Exception {
        SpringApplication app = new SpringApplication(SupplierApplication.class);
        app.setBannerMode(OFF);
        app.run(args);
    }

}
