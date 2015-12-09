package com.rafaelfiume.db.plugin.config;

import org.junit.Test;

public class DataSourceConfigTest {

    @Test
    public void checkNoExceptionIsThrownWhenTryingToGetADataSourceWithABlankUrl() {
        new DataSourceConfig().dataSource(" ");
    }
}
