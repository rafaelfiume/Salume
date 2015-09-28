package com.rafaelfiume.salume.test.support;

import org.springframework.boot.SpringApplication;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class ShutdownJettyTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void afterTestClass(TestContext testContext) throws Exception {
        SpringApplication.exit(testContext.getApplicationContext());
    }

}
