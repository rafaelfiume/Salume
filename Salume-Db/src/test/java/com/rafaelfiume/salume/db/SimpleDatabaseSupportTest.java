package com.rafaelfiume.salume.db;

import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpecRunner.class)
@ContextConfiguration(classes = DbApplication.class)
@Transactional
public class SimpleDatabaseSupportTest extends TestState {

    private String dbStatement;

    private SimpleDatabaseSupport dbSupport;

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    public void setSimpleDatabaseSupport(SimpleDatabaseSupport dbSupport) {
        this.dbSupport = dbSupport;
    }

    @Before
    public void dropDb() {
        dbSupport.dropDb("moviestore");
    }

    @Test
    public void createTableStatement() throws Exception {
        given(aStatement("CREATE TABLE moviestore.films (\n" +
                        "    code        char(5) CONSTRAINT firstkey PRIMARY KEY,\n" +
                        "    title       varchar(40) NOT NULL,\n" +
                        "    did         integer NOT NULL,\n" +
                        "    date_prod   date,\n" +
                        "    kind        varchar(10),\n" +
                        "    len         interval hour to minute\n" +
                        ");"));

        when(executingThatStatement());

        then(theTable(withSchema("moviestore"), andTableName("films")), exists());
    }

    private GivensBuilder aStatement(final String statement) {
        return givens -> {
            SimpleDatabaseSupportTest.this.dbStatement = statement;
            return givens;
        };
    }

    private ActionUnderTest executingThatStatement() {
        return (givens, capturedInputAndOutputs) -> {
            dbSupport.execute(dbStatement);
            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<Boolean> theTable(String schemaName, String tableName) {
        return inputAndOutputs -> {
            // http://stackoverflow.com/a/24089729
            String query = "SELECT EXISTS (\n" +
                    "    SELECT 1 \n" +
                    "    FROM   pg_catalog.pg_class c\n" +
                    "    JOIN   pg_catalog.pg_namespace n ON n.oid = c.relnamespace\n" +
                    "    WHERE  n.nspname = '" + schemaName + "'\n" +
                    "    AND    c.relname = '" + tableName + "'\n" +
                    "    AND    c.relkind = 'r'    -- only tables(?)\n" +
                    ");";

            return dbSupport.queryBoolean(query);
        };
    }

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

    private String withSchema(String name) {
        return name;
    }

    private String andTableName(String name) {
        return name;
    }

}
