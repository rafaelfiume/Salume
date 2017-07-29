package com.rafaelfiume.salume.acceptance;

import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.SupplierApplication;
import com.rafaelfiume.salume.config.MisconfiguredDataSourceConfig;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static com.rafaelfiume.salume.support.Applications.CLIENT;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

// It requires another container to run since it's using different configs (see line above)
@SpringBootTest(classes = {SupplierApplication.class, MisconfiguredDataSourceConfig.class}, webEnvironment = RANDOM_PORT)
@DirtiesContext // Closes the context and stops the container
public class StatusPageSadPathEndToEndTest extends AbstractSequenceDiagramTestState {

    private static final MediaType TEXT_PLAIN_CHARSET_UTF8 = parseMediaType("text/plain;charset=utf-8");

    private ResponseEntity<String> response;

    @LocalServerPort
    private int port;

    @Test
    public void showStatusFailWhenAppIsUpAndRunningAndDatabaseConnectionFails() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());
        and(databaseIsDown());

        when(aClientRequestsStatusPage());

        then(theContentType(), is(TEXT_PLAIN_CHARSET_UTF8));
        and(theStatusPage(), hasHttpStatusCode(OK));
        and(theStatusOfTheApp(), is("FAILING"));
        and(theDatabaseStatus(), is("FAILING"));
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", statusPageUri());

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
            this.response = new TestRestTemplate().getForEntity(statusPageUri(), String.class);

            captureRequest(withContent(statusPageUri()), from(CLIENT), to(SUPPLIER));

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpStatus> theStatusPage() {
        return inputAndOutputs -> {
            captureResponse(withContent(response.getBody()), from(SUPPLIER), to(CLIENT));

            return this.response.getStatusCode();
        };
    }

    private StateExtractor<String> theStatusOfTheApp() {
        return inputAndOutputs -> {
            String firstLine = this.response.getBody().split(lineSeparator())[0];
            return trim(firstLine.split("is:")[1]);
        };
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

    private String statusPageUri() {
        return "http://localhost:" + port + "/salume/supplier/status";
    }

}
