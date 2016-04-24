package com.rafaelfiume.db.plugin.database;

public class VersionBase { // TODO RF 24/04/2016 implements VersionRepository

    private SimpleJdbcDatabaseSupport db;

    public VersionBase(SimpleJdbcDatabaseSupport db) {
        this.db = db;
    }

    public String majorVersion() {
        return db.queryString("SELECT major FROM moviestore.version"); // TODO : RF : 23/04/2016 : Extract schema from the query
    }

    public String minorVersion() {
        return db.queryString("SELECT minor FROM moviestore.version"); // TODO : RF : 23/04/2016 : Extract schema from the query
    }

    public void updateMajorVersionTo(String newMajorVersion) {
        // assumes newMajorVersion is a valid version
        db.update("UPDATE moviestore.version set major = ?", newMajorVersion);
    }

    public void updateMinorVersionTo(String newMinorVersion) {
        // assumes newMinorVersion is a valid version
        db.update("UPDATE moviestore.version set minor = ?", newMinorVersion);
    }

}
