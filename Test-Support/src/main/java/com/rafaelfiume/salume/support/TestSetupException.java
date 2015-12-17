package com.rafaelfiume.salume.support;

public class TestSetupException extends RuntimeException {
    public TestSetupException(Throwable cause) {
        super("error during test setup", cause);
    }
}
