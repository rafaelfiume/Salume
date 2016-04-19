package com.rafaelfiume.db.plugin.sqlscripts;

import com.rafaelfiume.db.plugin.database.SimpleJdbcDatabaseSupport;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static com.rafaelfiume.db.plugin.database.DataSourceFactory.newDataSource;
import static java.lang.System.getenv;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class SimpleJdbcDatabaseSupportIntTest {

    public static final String MOVIE_STORE_SCHEMA = "moviestore";

    private final SimpleJdbcDatabaseSupport underTest = new SimpleJdbcDatabaseSupport(newDataSource(getenv("DATABASE_URL")));

    private String dbStatement;

    @Before
    public void dropDb() {
        underTest.dropAndCreate(MOVIE_STORE_SCHEMA);
    }

    @Test
    public void createTableStatement() throws Exception {
        given_aStatement("CREATE TABLE moviestore.films (\n" +
                "    code        char(5) CONSTRAINT firstkey PRIMARY KEY,\n" +
                "    title       varchar(40) NOT NULL,\n" +
                "    did         integer NOT NULL,\n" +
                "    date_prod   date,\n" +
                "    kind        varchar(10),\n" +
                "    len         interval hour to minute\n" +
                ");");

        when_executingThatStatement();

        then(theTable(withSchema(MOVIE_STORE_SCHEMA), andTableName("films")), exists());
    }

    @Test
    public void cleanTable() throws Exception {
        given_aCustomerTableWithThreeEntries();
        then(theCustomerTable(), hasSize(3));

        when_cleaningTheCustomerTable();
        then(theCustomerTable(), is(empty()));
    }

    private void given_aStatement(final String statement) {
        this.dbStatement = statement;
    }

    private void given_aCustomerTableWithThreeEntries() {
        underTest.execute("CREATE TABLE moviestore.customers (id int CONSTRAINT firstkey PRIMARY KEY, name varchar(40) NOT NULL);");
        underTest.execute("INSERT INTO moviestore.customers VALUES (1, 'Zen Kiwi');");
        underTest.execute("INSERT INTO moviestore.customers VALUES (2, 'Banana Pereira');");
        underTest.execute("INSERT INTO moviestore.customers VALUES (3, 'Watermelown Dias');");
    }

    private void when_executingThatStatement() {
        underTest.execute(dbStatement);
    }

    private void when_cleaningTheCustomerTable() {
        underTest.cleanTable("moviestore.customers");
    }

    private Boolean theTable(String schemaName, String tableName) {
        String query = "SELECT EXISTS (\n" +
                "    SELECT 1 \n" +
                "    FROM   pg_catalog.pg_class c \n" +
                "    JOIN   pg_catalog.pg_namespace n ON n.oid = c.relnamespace \n" +
                "    WHERE  n.nspname = '" + schemaName + "' \n" +
                "    AND    c.relname = '" + tableName + "' \n" +
                "    AND    c.relkind = 'r'    -- only tables(?) \n" +
                ");";

        return underTest.queryBoolean(query);
    }

    private List<Customer> theCustomerTable() {
        return underTest.query("SELECT * FROM moviestore.customers", new CustomerRowMapper());
    }

    // Decorators

    private String withSchema(String name) {
        return name;
    }

    private String andTableName(String name) {
        return name;
    }

    public static <T> void then(T actual, Matcher<? super T> matcher) {
        assertThat(actual, matcher);
    }

    // Matchers

    private Matcher<? super Boolean> exists() {
        return new TypeSafeMatcher<Boolean>() {
            @Override
            protected boolean matchesSafely(Boolean item) {
                return item;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("item doesn\'t exist");
            }
        };
    }

    // Helper classes

    static class Customer {}

    static class CustomerRowMapper implements RowMapper<Customer> {
        @Override
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Customer();
        }
    }

}
