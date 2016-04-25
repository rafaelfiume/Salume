package support;

import com.rafaelfiume.db.plugin.Version;
import com.rafaelfiume.db.plugin.sqlscripts.Script;
import org.hamcrest.Matcher;

import static com.rafaelfiume.db.plugin.Version.newVersion;
import static com.rafaelfiume.db.plugin.sqlscripts.Script.newScript;
import static org.junit.Assert.assertThat;

public final class Decorators {

    private Decorators() {
        // utility class
    }

    public static String majorVersion(String version) {
        return version;
    }

    public static String minorVersion(String version) {
        return version;
    }

    public static String and(String stuff) {
        return stuff;
    }

    public static <T> T to(T stuff) {
        return stuff;
    }

    public static <T> T with(T stuff) {
        return stuff;
    }

    public static Version version(String major, String minor) {
        return newVersion(major, minor);
    }

    public static Script script(String name) {
        return newScript(name);
    }

    public static <T> void then(T actual, Matcher<? super T> matcher) {
        assertThat(actual, matcher);
    }
}
