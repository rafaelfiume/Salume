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
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.rafaelfiume.salume.web.controllers.StatusPageController;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.DelegatingApplicationContextInitializer;
import org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.util.jar.Manifest;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.System.lineSeparator;
import static org.apache.commons.lang3.StringUtils.trim;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.parseMediaType;

@RunWith(SpecRunner.class)
public class StatusPageTest extends TestState implements WithCustomResultListeners {

    public static final String STATUS_PAGE_URI = "http://localhost:8080/salume/supplier/status";

    public static final MediaType TEXT_PLAIN_CHARSET_UTF8 = parseMediaType("text/plain;charset=utf-8");

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    private ResponseEntity<String> statusPageResponse;

    @Before
    public void setUp() {
        this.sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    @AfterClass
    public static void stopApps() {
        // TODO
    }

    @Test
    public void statusPageIsThere() throws Exception {
        given(salumeSupplierAppIsUpAndRunning());

        when(aClientRequestsStatusPage());

        then(theStatusPage(), hasHttpStatusCode(OK));
        then(theApplicantionNameAndVersion(), is("Salume Supplier DEV"));
        then(theStatusOfTheApp(), is("OK"));
        then(theAppVersionInTheStatusPage(), is(theImplementationVersionInTheManifest()));
        then(theContentType(), is(TEXT_PLAIN_CHARSET_UTF8));
    }

    private String theImplementationVersionInTheManifest() {
        Manifest manifest = StatusPageController.getManifest(SupplierApplication.class);
        return "Version: " + manifest.getMainAttributes().getValue("Implementation-Version");
    }

    private GivensBuilder salumeSupplierAppIsUpAndRunning() {
        return givens -> {
            givens.add("Supplier Status Page:", STATUS_PAGE_URI);

            SupplierApplication.main(new String[]{"--debug", "--profile=dev"});

            return givens;
        };
    }

    private ActionUnderTest aClientRequestsStatusPage() {
        return (givens, capturedInputAndOutputs) -> {
            this.statusPageResponse = new TestRestTemplate().getForEntity(STATUS_PAGE_URI, String.class);

            // this is what makes the sequence diagram magic happens
            capturedInputAndOutputs.add("Status Page Request from client to Supplier", STATUS_PAGE_URI);

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<HttpStatus> theStatusPage() {
        return inputAndOutputs -> {
            // this is what makes the sequence diagram magic happens
            capturedInputAndOutputs.add("Status Page Response from Supplier to client", statusPageResponse.getBody());

            return this.statusPageResponse.getStatusCode();
        };
    }

    private StateExtractor<String> theApplicantionNameAndVersion() {
        return inputAndOutputs -> {
            String firstLine = this.statusPageResponse.getBody().split(lineSeparator())[0];
            return trim(firstLine.split("is:")[0]);
        };
    }

    private StateExtractor<String> theStatusOfTheApp() {
        return inputAndOutputs -> {
            String firstLine = this.statusPageResponse.getBody().split(lineSeparator())[0];
            return trim(firstLine.split("is:")[1]);
        };
    }

    private StateExtractor<String> theAppVersionInTheStatusPage() {
        return inputAndOutputs -> this.statusPageResponse.getBody().split(lineSeparator())[1];
    }

    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> statusPageResponse.getHeaders().getContentType();
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
