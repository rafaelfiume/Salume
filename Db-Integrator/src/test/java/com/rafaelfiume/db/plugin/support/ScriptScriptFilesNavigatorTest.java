package com.rafaelfiume.db.plugin.support;

import org.junit.Test;

import java.net.URISyntaxException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ScriptScriptFilesNavigatorTest {

    @Test
    public void shouldReturnAllTheScriptFilesUnder_scripts_Directory() throws URISyntaxException {
        final ScriptFilesNavigator nav = new ScriptFilesNavigator();

        assertThat(nav.numberOfFiles(), is(3));

        assertThat(nav.hasNext(), is(true));
        assertThat(nav.next(), is("scripts/i01/01.create-a-table-here_script.sql"));

        assertThat(nav.hasNext(), is(true));
        assertThat(nav.next(), is("scripts/i01/02.doing-something-script.sql"));

        assertThat(nav.hasNext(), is(true));
        assertThat(nav.next(), is("scripts/i01/03.doing-something-else-script.sql"));

        assertThat(nav.hasNext(), is(false));
    }
}
