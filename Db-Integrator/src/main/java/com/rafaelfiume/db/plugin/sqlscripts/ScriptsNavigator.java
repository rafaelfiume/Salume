package com.rafaelfiume.db.plugin.sqlscripts;

public interface ScriptsNavigator {

    boolean hasNext();

    Script next();

    void close();

}
