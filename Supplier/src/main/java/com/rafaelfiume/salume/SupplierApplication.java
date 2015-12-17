package com.rafaelfiume.salume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class})
public class SupplierApplication {

    public static void main(String... args) throws Exception {
        SpringApplication app = new SpringApplication(SupplierApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}
