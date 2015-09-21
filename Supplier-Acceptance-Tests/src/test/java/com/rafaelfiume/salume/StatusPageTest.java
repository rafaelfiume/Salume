package com.rafaelfiume.salume;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.yatspec.junit.SpecResultListener;
import com.googlecode.yatspec.junit.SpecRunner;
import com.googlecode.yatspec.junit.WithCustomResultListeners;
import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramGenerator;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import com.googlecode.yatspec.rendering.html.DontHighlightRenderer;
import com.googlecode.yatspec.rendering.html.HtmlResultRenderer;
import com.googlecode.yatspec.rendering.html.index.HtmlIndexRenderer;
import com.googlecode.yatspec.rendering.html.tagindex.HtmlTagIndexRenderer;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import org.apache.http.HttpEntity;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.*;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.apache.commons.lang3.StringUtils.isNoneEmpty;

@RunWith(SpecRunner.class)
public class StatusPageTest extends TestState implements WithCustomResultListeners {

    public static final String STATUS_PAGE_URI = "http://localhost:8080/salume/supplier/status";
    public static final String HTTP_RESPONSE = "HTTP_RESPONSE";

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    private static final SupplierApplication SUPPLIER_APPLICATION = new SupplierApplication();

    @Before
    public void setUp() {
        this.sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @BeforeClass
    public static void startApp() throws Exception {
        // being started at given...
    }

    @AfterClass
    public static void stopApps() {
        // TODO
    }

    @Test
    public void statusPageIsThere() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());
        when(aClientRequestsStatusPage());
        then(theStatusPage(), hasHttpStatusCode(200));
        then(theStatusPage(), showsThatAppIsOk());
        then(theStatusPage(), showsCorrectAppVersion());
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", STATUS_PAGE_URI);
            SUPPLIER_APPLICATION.main();
            return givens;
        };
    }

    private ActionUnderTest aClientRequestsStatusPage() {
        return (givens, capturedInputAndOutputs) -> {

            HttpAppResponse appResponse = getHttpAppResponse(STATUS_PAGE_URI);
            // this is what makes the sequence diagram magic happens
            capturedInputAndOutputs.add("Status Page Request from client to Supplier", STATUS_PAGE_URI);
            capturedInputAndOutputs.add(HTTP_RESPONSE, appResponse);

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpAppResponse> theStatusPage() {
        return inputAndOutputs -> {
            HttpAppResponse appResponse = inputAndOutputs.getType(HTTP_RESPONSE, HttpAppResponse.class);

            capturedInputAndOutputs.add("Status Page Response from Supplier to client", appResponse.body());

            return appResponse;
        };
    }

    private Matcher<HttpAppResponse> showsThatAppIsOk() {
        return new TypeSafeMatcher<HttpAppResponse>() {
            @Override
            protected boolean matchesSafely(HttpAppResponse result) {
                String body = result.body();
                return isNoneEmpty(body) && body.contains("OK");
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("app is failing");
            }
        };
    }

    private Matcher<HttpAppResponse> hasHttpStatusCode(Integer expected) {
        return new TypeSafeMatcher<HttpAppResponse>() {
            @Override
            protected boolean matchesSafely(HttpAppResponse result) {
                return result.statusCode().equals(expected);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("wrong http status code");
            }
        };
    }

    private Matcher<HttpAppResponse> showsCorrectAppVersion() {
            return new TypeSafeMatcher<HttpAppResponse>() {
                @Override
                protected boolean matchesSafely(HttpAppResponse result) {
                    String body = result.body();
                    return isNoneEmpty(body) && body.contains("Version: DEV-SNAPSHOT");
                }

                @Override
                public void describeTo(Description description) {
                    description.appendText("wrong app version");
                }
            };
    }

    private HttpAppResponse getHttpAppResponse(String uri) throws IOException {
        HttpAppResponse appResponse;

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            HttpGet httpget = new HttpGet(uri);
            httpget.addHeader("accept", "text/text; charset=utf-8");

            System.out.println("Executing request " + httpget.getRequestLine());

            // Create a custom response handler
            ResponseHandler<HttpAppResponse> responseHandler = response -> {
                int status = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();
                return new HttpAppResponse(status, entity != null ? EntityUtils.toString(entity) : null);
            };

            appResponse = httpclient.execute(httpget, responseHandler);
        }
        return appResponse;
    }

    public static class HttpAppResponse {

        private final Integer statusCode;

        private final String body;

        HttpAppResponse(Integer statusCode, String body) {
            this.statusCode = statusCode;
            this.body = body;
        }

        public Integer statusCode() {
            return statusCode;
        }

        public String body() {
            return body;
        }
    }

    //////////////////// Test Infrastructure Stuff //////////////

    @After
    public void generateSequenceDiagram() {
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
        capturedInputAndOutputs.add("Sequence Diagram", sequenceDiagramGenerator.generateSequenceDiagram(messages));
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows()).
                        withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer()),
                new HtmlIndexRenderer()).
                safeCast(SpecResultListener.class);
    }

}
