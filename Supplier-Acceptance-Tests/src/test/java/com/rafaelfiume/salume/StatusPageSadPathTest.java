package com.rafaelfiume.salume;

import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.config.MisconfiguredDataSourceConfig;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.web.controllers.StatusPageController;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.jar.Manifest;

import static com.rafaelfiume.salume.support.Applications.CLIENT;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@SpringApplicationConfiguration(classes = {SupplierApplication.class, MisconfiguredDataSourceConfig.class})
@WebIntegrationTest(value = "server.port=8282") // It requires another container to run since it's using different configs (see line above)
@DirtiesContext // Closes the context and stops the container
public class StatusPageSadPathTest extends AbstractSequenceDiagramTestState {

    public static final String STATUS_PAGE_URI = "http://localhost:8282/salume/supplier/status"; // using different port

    private static final MediaType TEXT_PLAIN_CHARSET_UTF8 = parseMediaType("text/plain;charset=utf-8");

    private ResponseEntity<String> response;

    @Test
    public void showStatusFailWhenAppIsUpAndRunningAndDatabaseConnectionFails() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());
        and(databaseIsDown());

        when(aClientRequestsStatusPage());

        then(theContentType(), is(TEXT_PLAIN_CHARSET_UTF8));
        then(theStatusPage(), hasHttpStatusCode(OK));
        then(theApplicantionNameAndVersion(), is("Salume Supplier DEV"));
        then(theStatusOfTheApp(), is("FAILING"));
        then(theAppVersionInTheStatusPage(), is(theImplementationVersionInTheManifest()));
        then(theDatabaseStatus(), is("FAILING"));
    }

    private String theImplementationVersionInTheManifest() {
        Manifest manifest = StatusPageController.getManifest(SupplierApplication.class);
        return "Version: " + manifest.getMainAttributes().getValue("Implementation-Version");
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", STATUS_PAGE_URI);

//          App initialized by Spring Boot.

            return givens;
        };
    }

    private GivensBuilder databaseIsDown() {
        return givens -> {

//          Database is misconfigured. @See MisconfiguredDataSourceConfig
//          And, yes, misconfigured is a world ;) => http://www.oxforddictionaries.com/definition/english/misconfigure

            return givens;
        };
    }

    private ActionUnderTest aClientRequestsStatusPage() {
        return (givens, capturedInputAndOutputs) -> {
            this.response = new TestRestTemplate().getForEntity(STATUS_PAGE_URI, String.class);

            capture("Status page request", withContent(STATUS_PAGE_URI), from(CLIENT), to(SUPPLIER));

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpStatus> theStatusPage() {
        return inputAndOutputs -> {
            capture("Status page response ", withContent(response.getBody()), from(SUPPLIER), to(CLIENT));

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
