package com.rafaelfiume.salume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SupplierApplication {

    public static void main(String... args) throws Exception {
        SpringApplication app = new SpringApplication(SupplierApplication.class);
        app.setShowBanner(false);
        app.run(args);
    }

}
