package com.rafaelfiume.salume.config;

import org.springframework.boot.SpringApplication;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

// Duplicated in Rainbow
public class ShutdownJettyTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        SpringApplication.exit(testContext.getApplicationContext());
    }

}
