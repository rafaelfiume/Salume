package com.rafaelfiume.db.plugin.config;

import org.junit.Test;

import static org.junit.Assert.fail;

public class DataSourceConfigTest {

    @Test
    public void checkNoExceptionIsThrownWhenTryingToGetADataSourceWithABlankUrl() {
        try {
            new DataSourceConfig().dataSource(" ");
        } catch (Exception e) {
            fail("shouldn´t have thrown any exception");
        }
    }
}
