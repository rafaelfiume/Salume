package com.rafaelfiume.db.plugin.sqlscripts;

import java.io.Closeable;

public interface ScriptsNavigator extends Closeable {

    boolean hasNext();

    String next();

}
