package com.rafaelfiume.db.plugin.support;

public interface SimpleDatabaseSupport {

    void dropAndCreate(String schema);

    void execute(String statement);
}
