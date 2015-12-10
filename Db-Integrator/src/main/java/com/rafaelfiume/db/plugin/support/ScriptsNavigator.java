package com.rafaelfiume.db.plugin.support;

import java.io.Closeable;

public interface ScriptsNavigator extends Closeable {

    boolean hasNext();

    String next();

}
