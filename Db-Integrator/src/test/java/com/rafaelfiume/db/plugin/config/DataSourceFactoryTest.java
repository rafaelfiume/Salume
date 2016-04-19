package com.rafaelfiume.db.plugin.config;

import org.junit.Test;

import static com.rafaelfiume.db.plugin.config.DataSourceFactory.newDataSource;
import static org.junit.Assert.fail;

public class DataSourceFactoryTest {

    public static final String BLANK_DATABASE_URL = " ";

    @Test
    public void checkNoExceptionIsThrownWhenTryingToGetADataSourceWithABlankUrl() {
        try {
            newDataSource(BLANK_DATABASE_URL);
        } catch (Exception e) {
            fail("shouldn´t have thrown any exception");
        }
    }
}
