package support;

import org.hamcrest.Matcher;

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

    public static String to(String stuff) {
        return stuff;
    }

    public static <T> void then(T actual, Matcher<? super T> matcher) {
        assertThat(actual, matcher);
    }
}
