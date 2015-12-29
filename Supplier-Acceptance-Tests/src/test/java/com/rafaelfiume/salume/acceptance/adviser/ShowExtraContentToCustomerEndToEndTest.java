package com.rafaelfiume.salume.acceptance.adviser;

import com.googlecode.yatspec.junit.LinkingNote;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.db.advisor.PersistentProductBase;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.ProductBuilder;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.support.transactions.SpringCommitsAndClosesTestTransactionTransactor;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;

import static com.rafaelfiume.salume.domain.ProductBuilder.a;
import static com.rafaelfiume.salume.support.Applications.CUSTOMER;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static com.rafaelfiume.salume.support.Xml.*;
import static java.lang.String.format;
import static javax.xml.xpath.XPathConstants.NODESET;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@Ignore
@LinkingNote(message = "Related to %s", links = AdviseProductBasedOnCustomerProfileEndToEndTest.class)
@Transactional
public class ShowExtraContentToCustomerEndToEndTest extends AbstractSequenceDiagramTestState {

    private ResponseEntity<String> response;

    private MoneyDealer moneyDealer;

    private final SpringCommitsAndClosesTestTransactionTransactor transactor = new SpringCommitsAndClosesTestTransactionTransactor();
    private JdbcTemplate jdbcTemplate;
    private PersistentProductBase productBase;

    @Autowired
    public void setMoneyDealer(MoneyDealer moneyDealer) {
        this.moneyDealer = moneyDealer;
    }

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setProductBase(PersistentProductBase productBase) {
        this.productBase = productBase;
    }

    @Test
    public void showToCustomersProductImageAndDescriptionSoTheyCanHaveABetterIdeaOfWhatTheyAreBuying() throws Exception {
        given(theAvailabilityOf(a("Chouriço Português").withVariety("Chorizo")));

        when(requestingBestOfferForACustomer());

        then(theSuggestionForCustomer(), containsAn(imageLinkWithUrl("https://upload.wikimedia.org/wikipedia/commons/3/3f/Palacioschorizo.jpg"),
                andAProductDescriptionLinkWithUrl("https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles=Chorizo")));
    }

    private GivensBuilder theAvailabilityOf(ProductBuilder... products) {
        // Logic duplicated from AdviseProductBasedOnCustomerProfileEndToEndTest
        return givens -> {
            transactor.perform(() -> {
                deleteFromTables(jdbcTemplate, "salumistore.products");

                for (ProductBuilder p : products) {
                    productBase.add(p.build(moneyDealer));
                }
            });

            return givens;
        };
    }


    private ActionUnderTest requestingBestOfferForACustomer() {
    return (givens, capturedInputAndOutputs1) -> {
        // TODO RF 20/10/2015 Extract the server address to a method in the abstract class
        final String adviserUrl = "http://localhost:8081/salume/supplier/advise/for/Magic";

        this.response = new TestRestTemplate().getForEntity(adviserUrl, String.class);

        capture("Salume advice request", withContent(adviserUrl), from(CUSTOMER), to(SUPPLIER));

        return capturedInputAndOutputs;
        };
    }

    private StateExtractor<Node> theSuggestionForCustomer() throws Exception {
    capture("Salume advice response", withContent(prettyPrint(xmlFrom(response.getBody()))), from(SUPPLIER), to(CUSTOMER));

    return inputAndOutputs -> ((NodeList)
    xpath().evaluate("//product", xmlFrom(response.getBody()), NODESET)
    ).item(0);
}
    //
    // Method decorators
    //

    private String imageLinkWithUrl(String url) {
        return url;
    }

    private String andAProductDescriptionLinkWithUrl(String url) {
        return url;
    }

    //
    // Matchers
    //

    private TypeSafeMatcher<Node> containsAn(String expectedImageUrl, String expectedDescriptionUrl) {
        return new TypeSafeMatcher<Node>() {
            @Override
            protected boolean matchesSafely(Node xml) {
                return expectedImageUrl.equals(actualImageUrlFrom(xml)) && expectedDescriptionUrl.endsWith(actualDescriptionUrlFrom(xml));
            }

            @Override
            public void describeTo(Description description) {
                description.appendText(format("a product with image url \"%s\", and description url \"%s\"", expectedImageUrl, expectedDescriptionUrl));
            }

            @Override
            protected void describeMismatchSafely(Node xml, Description mismatchDescription) {
                mismatchDescription.appendText(format("product had image url \"%s\", and description url \"%s\"", actualImageUrlFrom(xml), actualDescriptionUrlFrom(xml)));}

            private String actualImageUrlFrom(Node xml)       { return getValueFrom(xml, "image-url"); }
            private String actualDescriptionUrlFrom(Node xml) { return getValueFrom(xml, "product-description-url"); }
        };
    }
}
