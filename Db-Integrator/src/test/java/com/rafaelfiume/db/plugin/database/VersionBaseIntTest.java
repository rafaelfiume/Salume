package com.rafaelfiume.db.plugin.database;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.db.plugin.database.VersionBase;
import org.junit.Before;
import org.junit.Test;

import static com.rafaelfiume.db.plugin.Version.newVersion;
import static com.rafaelfiume.db.plugin.database.DataSourceFactory.newDataSource;
import static java.lang.String.format;
import static java.lang.System.getenv;
import static org.hamcrest.Matchers.is;
import static support.Decorators.majorVersion;
import static support.Decorators.minorVersion;
import static support.Decorators.then;

public class VersionBaseIntTest {

    public static final String MOVIE_STORE_SCHEMA = "moviestore";

    private final SimpleJdbcDatabaseSupport db = new SimpleJdbcDatabaseSupport(newDataSource(getenv("DATABASE_URL")));
    private final VersionBase underTest = new VersionBase(db);

    @Before
    public void dropDb() {
        db.dropAndCreate(MOVIE_STORE_SCHEMA);
    }

    @Test
    public void updatesMajorAndMinorVersion() {
        given_aSchemaWithCurrent(majorVersion("i23"), minorVersion("12"));

        // when...
        underTest.updateMajorVersionTo("i25");
        underTest.updateMinorVersionTo("05");

        then(underTest.currentVersion(), is(newVersion("i25", "05")));
    }

    private void given_aSchemaWithCurrent(String majorVersion, String minorVersion) {
        db.execute("CREATE TABLE moviestore.version (\n" +
                "    major       varchar(5) NOT NULL,\n" +
                "    minor       varchar(2) NOT NULL\n" +
                ");");
        db.execute(format("INSERT INTO moviestore.version VALUES ('%s', '%s');", majorVersion, minorVersion));
    }
}
