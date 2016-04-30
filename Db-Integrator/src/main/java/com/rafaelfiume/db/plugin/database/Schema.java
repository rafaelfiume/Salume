package com.rafaelfiume.db.plugin.database;

public class Schema {

    private final String name;

    public static Schema schema(String name) {
        return new Schema(name);
    }

    private Schema(String name) {
        this.name = name;
    }

    public String name() { return name; }

    @Override
    public String toString() {
        return name;
    }
}
