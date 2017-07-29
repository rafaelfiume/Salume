package com.rafaelfiume.salume.support;

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
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.rafaelfiume.salume.SupplierApplication;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.rafaelfiume.salume.support.ContractApiWriter.writeApiRequestContractExample;
import static com.rafaelfiume.salume.support.ContractApiWriter.writeApiResponseContractExample;
import static java.lang.String.format;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpecRunner.class)
@SpringBootTest(classes = SupplierApplication.class, webEnvironment = DEFINED_PORT)
@ActiveProfiles("dev")
public class AbstractSequenceDiagramTestState extends TestState implements WithCustomResultListeners {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Rule
    public final TestName name = new TestName();

    private SequenceDiagramGenerator sequenceDiagramGenerator;

    @Before
    public final void setUp() {
        this.sequenceDiagramGenerator = new SequenceDiagramGenerator();
    }

    //
    // Yatspec Setup
    //

    @After
    public final void generateSequenceDiagram() {
        Sequence<SequenceDiagramMessage> messages = sequence(new ByNamingConventionMessageProducer().messages(capturedInputAndOutputs));
        capturedInputAndOutputs.add("Sequence Diagram", sequenceDiagramGenerator.generateSequenceDiagram(messages));
    }

    @Override
    public Iterable<SpecResultListener> getResultListeners() throws Exception {
        return sequence(
                new HtmlResultRenderer().
                        withCustomHeaderContent(SequenceDiagramGenerator.getHeaderContentForModalWindows()).
                        withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer<>()),
                new HtmlIndexRenderer()).
                safeCast(SpecResultListener.class);
    }

    //
    // Helpers
    //

    protected void captureRequest(String content, Applications from, Applications to) {
        makeSequenceDiagramHappen("request", content, from, to);
        writeApiRequestContractExample(content, getClass().getSimpleName(), name.getMethodName());
    }

    protected void captureResponse(String content, Applications from, Applications to) {
        makeSequenceDiagramHappen("response", content, from, to);
        writeApiResponseContractExample(content, getClass().getSimpleName(), name.getMethodName());
    }

    private void makeSequenceDiagramHappen(String requestOrResponse, String content, Applications from, Applications to) {
        capturedInputAndOutputs.add(format("%s from %s to %s", requestOrResponse, from.appName(), to.appName()), content);
    }

    protected <ItemOfInterest> TestState and(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
        return then(extractor, matcher);
    }

    protected String withContent(String content) {
        return content;
    }

    protected Applications from(Applications from) {
        return from;
    }

    protected Applications to(Applications to) {
        return to;
    }

}
