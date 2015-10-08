package com.rafaelfiume.salume;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.web.controllers.StatusPageController;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.jar.Manifest;

import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@Notes("<h3>What Is an Acceptance Test (not a stupid question), And How Is This Supposed to Work?</h3>" +
        "" +
        "Before implementing a feature, we write a test that will both:\n" +
        "a) describe the feature being implemented in an human readable and understandable way, which will help close the gaps between business and developers, and\n" +
        "b) work as a regression test suit.\n\n" +
        "" +
        "We start writing a business focused happy path test, from the input to the output.\n" +
        "At first, we don't mind if the code compile, and write a meaningful business point-of-view description of what the app should do.\n\n" +
        "" +
        "Then we do the necessary job to see the test failing, and assure that the failing messages are meaningful.\n" +
        "(Very helpful since it will help us catch problems quickly as they happen in the future.)\n\n" +
        "" +
        "The test will keep failing till the feature it describes is implemented, when it will be ready to be deployed into the staging environment(s).\n" +
        "If business is happy and the new feature is bug free, it's ready to be deployed into production (there may be additional steps depending on the work environment).\n" +
        "" +
        "<h3>Walking Skeleton</h3>" +
        "Besides being the acceptance test for the status page, this test was also part of a walking skeleton for this app (more details about what that means <a href=\"https://rafaelfiume.wordpress.com/2015/09/30/iteration-0/\" target=\"blank\">here</a>).\n\n\"" +
        "" +
        "The more experience I have, the more I focus on the way we work instead of the technologies we use.\n" +
        "I recommend reading the excellent <a href=\"http://www.growing-object-oriented-software.com\" target=\"blank\">Growing Object-Oriented Software Guided by Tests</a>."
)
public class StatusPageHappyPathTest extends AbstractSequenceDiagramTestState {

    public static final String STATUS_PAGE_URI = "http://localhost:8081/salume/supplier/status";

    private static final MediaType TEXT_PLAIN_CHARSET_UTF8 = parseMediaType("text/plain;charset=utf-8");

    private ResponseEntity<String> response;

    @Test
    public void appIsOk() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());

        when(aClientRequestsStatusPage());

        then(theContentType(), is(TEXT_PLAIN_CHARSET_UTF8));
        then(theStatusPage(), hasHttpStatusCode(OK));
        then(theApplicantionNameAndVersion(), is("Salume Supplier DEV"));
        then(theStatusOfTheApp(), is("OK"));
        then(theAppVersionInTheStatusPage(), is(theImplementationVersionInTheManifest()));
        then(theDatabaseStatus(), is("OK"));
    }

    private String theImplementationVersionInTheManifest() {
        Manifest manifest = StatusPageController.getManifest(SupplierApplication.class);
        return "Version: " + manifest.getMainAttributes().getValue("Implementation-Version");
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", STATUS_PAGE_URI);

//          App initialized by Spring Boot. Check http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html#boot-features-testing-spring-boot-applications
//          SupplierApplication.main(new String[]{"--debug", "--profile=dev"});

            return givens;
        };
    }

    private ActionUnderTest aClientRequestsStatusPage() {
        return (givens, capturedInputAndOutputs) -> {
            this.response = new TestRestTemplate().getForEntity(STATUS_PAGE_URI, String.class);

            // this is what makes the sequence diagram magic happens
            capturedInputAndOutputs.add("Status Page request from client to Supplier", STATUS_PAGE_URI);

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpStatus> theStatusPage() {
        return inputAndOutputs -> {
            // this is what makes the sequence diagram magic happens
            capturedInputAndOutputs.add("Status Page response from Supplier to client", response.getBody());

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

}