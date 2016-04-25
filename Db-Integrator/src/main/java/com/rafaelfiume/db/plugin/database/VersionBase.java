package com.rafaelfiume.db.plugin.database;

import com.rafaelfiume.db.plugin.Version;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.rafaelfiume.db.plugin.Version.newVersion;

public class VersionBase { // TODO RF 24/04/2016 implements VersionRepository

    private SimpleJdbcDatabaseSupport db;

    public VersionBase(SimpleJdbcDatabaseSupport db) {
        this.db = db;
    }

    public Version currentVersion() { // TODO : RF : 23/04/2016 : Extract schema from the query
        return db.queryObject("SELECT major, minor FROM moviestore.version", new VersionRowMapper());
    }

    public void updateVersionTo(Version version) {
        db.update("UPDATE moviestore.version set major = ?, minor = ?", version.major(), version.minor());
    }

    static class VersionRowMapper implements RowMapper<Version> {

        @Override
        public Version mapRow(ResultSet rs, int i) throws SQLException {
            return newVersion(rs.getString("major"), rs.getString("minor"));
        }
    }

}
