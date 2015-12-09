package com.rafaelfiume.db.plugin.support;

public interface SimpleDatabaseSupport {

    void dropDb(String schema);

    void execute(String statement);
}
