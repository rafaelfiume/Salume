package com.rafaelfiume.salume.acceptance.adviser;

import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.db.PersistentProductBase;
import com.rafaelfiume.salume.db.PersistentVarietyBase;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.ProductBuilder;
import com.rafaelfiume.salume.domain.VarietyBuilder;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.support.transactions.SpringCommitsAndClosesTestTransactionTransactor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;

import static com.rafaelfiume.salume.domain.ProductBuilder.a;
import static com.rafaelfiume.salume.domain.VarietyBuilder.ofSalumi;
import static com.rafaelfiume.salume.support.Applications.CUSTOMER;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static com.rafaelfiume.salume.support.Xml.getValueFrom;
import static com.rafaelfiume.salume.support.Xml.prettyPrint;
import static com.rafaelfiume.salume.support.Xml.xmlFrom;
import static com.rafaelfiume.salume.support.Xml.xpath;
import static java.lang.String.format;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@LinkingNote(message = "See also %s", links = AdviseProductBasedOnCustomerProfileEndToEndTest.class)
@Transactional
public class ShowExtraContentToCustomerEndToEndTest extends AbstractSequenceDiagramTestState {

    private ResponseEntity<String> response;

    @SuppressWarnings("unused")
    @Autowired private MoneyDealer moneyDealer;

    private final SpringCommitsAndClosesTestTransactionTransactor transactor = new SpringCommitsAndClosesTestTransactionTransactor();
    private JdbcTemplate jdbcTemplate;

    @SuppressWarnings("unused")
    @Autowired private PersistentVarietyBase varietyBase;

    @SuppressWarnings("unused")
    @Autowired private PersistentProductBase productBase;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Before
    public void cleanUpTables() {
        deleteFromTables(jdbcTemplate, "salumistore.products", "salumistore.variety");
    }

    @Test
    public void showToCustomersProductImageAndDescriptionSoTheyCanHaveABetterIdeaOfWhatTheyAreBuying() throws Exception {
        given(theAvailabilityOf(a("Salame 'Nduja Calabrese")
                .with(variety(ofSalumi("'Nduja").withImageLink("0/00/Nduja.jpg"))))
        );

        when(requestingBestOfferForACustomer());

        then(theSuggestionForCustomer(), isA("'Nduja", typeOfSalumi(),
                andContainsAnImageLinkWithUrl("https://upload.wikimedia.org/wikipedia/commons/0/00/Nduja.jpg"),
                andAProductDescriptionLinkWithUrl("https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles='Nduja")));
    }

    @Test
    public void returnDefaultImageUrlWhenThereIsNoImageLink() throws Exception {
        given(theAvailabilityOf(a("Salame 'Nduja Calabrese")
                .with(variety(ofSalumi("'Nduja").withNoImageLink())))
        );

        when(requestingBestOfferForACustomer());

        then(theSuggestionForCustomer(), isA("'Nduja", typeOfSalumi(),
                andContainsAnImageLinkWithUrl("https://upload.wikimedia.org/wikipedia/commons/b/b5/Formaggi_e_salumi_sardi.jpg"),
                andAProductDescriptionLinkWithUrl("https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles='Nduja")));
    }

    private GivensBuilder theAvailabilityOf(ProductBuilder... products) {
        return givens -> {
            transactor.perform(() -> {
                for (ProductBuilder p : products) {
                    productBase.add(p.build(moneyDealer));
                }
            });
            return givens;
        };
    }

    private VarietyBuilder variety(VarietyBuilder variety) {
        varietyBase.add(variety.build());
        return variety;
    }

    private ActionUnderTest requestingBestOfferForACustomer() {
        return (givens, capturedInputAndOutputs1) -> {
            // TODO RF 20/10/2015 Extract the server address to a method in the abstract class
            final String adviserUrl = "http://localhost:8081/salume/supplier/advise/for/Magic";

            this.response = new TestRestTemplate().getForEntity(adviserUrl, String.class);

            captureRequest(withContent(adviserUrl), from(CUSTOMER), to(SUPPLIER));

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<Node> theSuggestionForCustomer() throws Exception {
        captureResponse(withContent(prettyPrint(xmlFrom(response.getBody()))), from(SUPPLIER), to(CUSTOMER));

        return inputAndOutputs -> ((NodeList)
                xpath().evaluate("//product", xmlFrom(response.getBody()), NODESET)
        ).item(0);
    }
    //
    // Method decorators
    //

    private Void typeOfSalumi() {
        return null;
    }

    private String andContainsAnImageLinkWithUrl(String url) {
        return url;
    }

    private String andAProductDescriptionLinkWithUrl(String url) {
        return url;
    }

    //
    // Matchers
    //

    private TypeSafeMatcher<Node> isA(String expectedTypeOfSalumi, @SuppressWarnings("unused") Void blablabla, String expectedImageUrl, String expectedDescriptionUrl) {
        return new TypeSafeMatcher<Node>() {
            @Override
            protected boolean matchesSafely(Node xml) {
                return expectedTypeOfSalumi.equals(actualTypeOfSalumiFrom(xml))
                        && expectedImageUrl.equals(actualImageUrlFrom(xml))
                        && expectedDescriptionUrl.endsWith(actualDescriptionUrlFrom(xml));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(format(
                        "a product with variety \"%s\", image url \"%s\", and description url \"%s\"", expectedTypeOfSalumi, expectedImageUrl, expectedDescriptionUrl));
            }

            @Override
            protected void describeMismatchSafely(Node xml, Description mismatchDescription) {
                mismatchDescription.appendText(format(
                        "product had variety \"%s\", image url \"%s\", and description url \"%s\"", actualTypeOfSalumiFrom(xml), actualImageUrlFrom(xml), actualDescriptionUrlFrom(xml)));
            }

            private String actualTypeOfSalumiFrom(Node xml)   { return getValueFrom(xml, "variety"); }
            private String actualImageUrlFrom(Node xml)       { return getValueFrom(xml, "image"); }
            private String actualDescriptionUrlFrom(Node xml) { return getValueFrom(xml, "description"); }
        };
    }
}
