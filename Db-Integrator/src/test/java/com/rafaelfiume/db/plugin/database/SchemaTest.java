package com.rafaelfiume.db.plugin.database;

import org.junit.Test;

import static com.rafaelfiume.db.plugin.database.Schema.schema;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SchemaTest {

    @Test
    public void toStringReturnsTheNameOfTheSchema() throws Exception {
        assertThat(schema("music").toString(), is("music"));
    }
}