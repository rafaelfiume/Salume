package com.rafaelfiume.salume.acceptance;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.rafaelfiume.salume.support.Applications.CLIENT;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@Notes("<h3>What Is an Acceptance Test (not a stupid question)?</h3>" +
        "" +
        "Before implementing a feature, we write a test that will both:\n" +
        "a) describe the feature being implemented in an human readable and understandable way to help close the gaps between business and developers, and\n" +
        "b) work as a regression test suit.\n\n" +
        "" +
        "This is and end-to-end test that shouldn't need to change if a database is replaced by another, if a broker is introduced in a messaging layer,\n" +
        "and so on (even if those changes would probably break the test) because it describes a feature from a high level business point-of-view.\n\n" +
        "" +
        "At first, we don't mind if the code compile, and start writing a consistent business focused happy path test, from the input to the output.\n" +
        "Then we do the necessary job to see the test failing, and assure that the failing messages are meaningful\n" +
        "(very helpful since it will help us catch problems quickly as they happen in the future.)\n\n" +
        "" +
        "The test will keep failing till the feature it describes is implemented, when the app will be ready to be deployed into the staging environment(s).\n" +
        "If business is happy and the new feature is bug free, it's ready to be deployed into production (there may be additional steps depending on the work environment).\n" +
        "" +
        "<h3>Walking Skeleton</h3>" +
        "Besides being the acceptance test for the status page, this test was also part of a walking skeleton for this app (more details about what that means <a href=\"https://rafaelfiume.wordpress.com/2015/09/30/iteration-0/\" target=\"blank\">here</a>).\n\n\"" +
        "" +
        "The more experience I have, the more I focus on the way we work instead of the technologies we use.\n" +
        "I recommend reading the excellent <a href=\"http://www.growing-object-oriented-software.com\" target=\"blank\">Growing Object-Oriented Software Guided by Tests</a>."
)
public class StatusPageHappyPathEndToEndTest extends AbstractSequenceDiagramTestState {

    private static final String STATUS_PAGE_URI = "http://localhost:8081/salume/supplier/status";

    private static final MediaType TEXT_PLAIN_CHARSET_UTF8 = parseMediaType("text/plain;charset=utf-8");

    private ResponseEntity<String> response;

    @Test
    public void showStatusOkWhenAppIsUpAndRunningAndAllTheResourcesItDependsOnAreAvailable() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());
        and(lastSuccessfulBuildNumberIs(233));

        when(aClientRequestsStatusPage());

        then(theContentType(), is(TEXT_PLAIN_CHARSET_UTF8));
        and(theStatusPage(), hasHttpStatusCode(OK));
        and(theApplicantionNameAndVersion(), is("Salume Supplier DEV"));
        and(theStatusOfTheApp(), is("OK"));
        and(theAppVersionInTheStatusPage(), is(buildNumber(233)));
        and(theDatabaseStatus(), is("OK"));
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", STATUS_PAGE_URI);

//          App initialized by Spring Boot. Check http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications
//          SupplierApplication.main(new String[]{"--debug", "--profile=dev"});

            return givens;
        };
    }

    private GivensBuilder lastSuccessfulBuildNumberIs(Integer build) {
        return givens -> {
            Map<String, String> lastBuild = new HashMap<String, String>() {{ put("BUILD_NUMBER", build.toString()); }};
            setEnv(lastBuild);
            return givens;
        };
    }

    private ActionUnderTest aClientRequestsStatusPage() {
        return (givens, capturedInputAndOutputs) -> {
            this.response = new TestRestTemplate().getForEntity(STATUS_PAGE_URI, String.class);

            captureRequest(withContent(STATUS_PAGE_URI), from(CLIENT), to(SUPPLIER));

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpStatus> theStatusPage() {
        return inputAndOutputs -> {
            captureResponse(withContent(response.getBody()), from(SUPPLIER), to(CLIENT));

            return this.response.getStatusCode();
        };
    }

    private StateExtractor<String> theApplicantionNameAndVersion() {
        return inputAndOutputs -> {
            String firstLine = this.response.getBody().split(lineSeparator())[0];
            return trim(firstLine.split("is:")[0]);
        };
    }

    private StateExtractor<String> theStatusOfTheApp() {
        return inputAndOutputs -> {
            String firstLine = this.response.getBody().split(lineSeparator())[0];
            return trim(firstLine.split("is:")[1]);
        };
    }

    private StateExtractor<String> theAppVersionInTheStatusPage() {
        return inputAndOutputs -> this.response.getBody().split(lineSeparator())[1];
    }

    private StateExtractor<String> theDatabaseStatus() {
        return inputAndOutputs -> {
            String thirdLine = this.response.getBody().split(lineSeparator())[2];
            return trim(thirdLine.split("is:")[1]);
        };
    }

    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> response.getHeaders().getContentType();
    }

    private Matcher<HttpStatus> hasHttpStatusCode(HttpStatus expected) {
        return new TypeSafeMatcher<HttpStatus>() {
            @Override
            protected boolean matchesSafely(HttpStatus result) {
                return expected == result;
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(expected);
            }
        };
    }

    private String buildNumber(int build) {
        return "Version: " + build;
    }

    // Really boring stuff to code! Glad I found this massive hack in the web...
    // Thank you guys => Source: https://stackoverflow.com/a/7201825
    private void setEnv(Map<String, String> newenv)
    {
        try
        {
            Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
            Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
            theEnvironmentField.setAccessible(true);
            Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
            env.putAll(newenv);
            Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
            theCaseInsensitiveEnvironmentField.setAccessible(true);
            Map<String, String> cienv = (Map<String, String>)     theCaseInsensitiveEnvironmentField.get(null);
            cienv.putAll(newenv);
        }
        catch (NoSuchFieldException e)
        {
            try {
                Class[] classes = Collections.class.getDeclaredClasses();
                Map<String, String> env = System.getenv();
                for(Class cl : classes) {
                    if("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
                        Field field = cl.getDeclaredField("m");
                        field.setAccessible(true);
                        Object obj = field.get(env);
                        Map<String, String> map = (Map<String, String>) obj;
                        map.clear();
                        map.putAll(newenv);
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

}
