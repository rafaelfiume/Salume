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
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.rafaelfiume.salume.SupplierApplication;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.replace;
import static org.apache.commons.lang3.StringUtils.trim;

@RunWith(SpecRunner.class)
@SpringApplicationConfiguration(classes = SupplierApplication.class)
@WebIntegrationTest(/*"debug=true"*/)
@ActiveProfiles("dev")
public class AbstractSequenceDiagramTestState extends TestState implements WithCustomResultListeners {

    private static final String GENERATED_FILES_DIRECTORY = "input-output-examples";

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

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
                        withCustomRenderer(SvgWrapper.class, new DontHighlightRenderer()),
                new HtmlIndexRenderer()).
                safeCast(SpecResultListener.class);
    }

    //
    // Helpers
    //

    protected void capture(String stuffBeingCaptured, String content, Applications from, Applications to) {
        // this is what makes the sequence diagram magic happens
        capturedInputAndOutputs.add(format("%s from %s to %s", stuffBeingCaptured, from.appName(), to.appName()), content);
        saveCaptured(content, withNameUsing(stuffBeingCaptured, from, to));
    }

    private void saveCaptured(String content, String fileName) {
        try {
            FileUtils.write(new File(fileName), content, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String withNameUsing(String stuffBeingCaptured, Applications from, Applications to) {
        return trim(format("%s/%s_%s_from_%s_to_%s.txt",
                GENERATED_FILES_DIRECTORY,
                getClass().getCanonicalName(),
                replace(stuffBeingCaptured, SPACE, "_"),
                from.appName(),
                to.appName()));
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
