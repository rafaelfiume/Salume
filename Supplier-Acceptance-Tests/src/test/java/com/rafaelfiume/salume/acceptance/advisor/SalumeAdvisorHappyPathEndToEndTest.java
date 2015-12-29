package com.rafaelfiume.salume.acceptance.advisor;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.db.advisor.PersistentProductBase;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.ProductBuilder;
import com.rafaelfiume.salume.matchers.AbstractAdvisedProductMatcherBuilder;
import com.rafaelfiume.salume.matchers.AdvisedProductMatcher;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.support.transactions.SpringCommitsAndClosesTestTransactionTransactor;
import com.rafaelfiume.salume.web.result.ReputationRepresentation;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
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
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.NUMBER;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "" +
        "Gioseppo select the customer profile when offering products to his customers. See the details about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
@Transactional
public class SalumeAdvisorHappyPathEndToEndTest extends AbstractSequenceDiagramTestState {

    private static final MediaType APPLICATION_XML_CHARSET_UTF8 = parseMediaType("application/xml;charset=utf-8");

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
    public void suggestToCustomerWithMagicProfileTheCheapestProducts() throws Exception {
        given(theAvailableProductsAre(
                a("(1st Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("(2nd Cheapest) Salume")       .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("(Expensive) & Light")         .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat(),
                a("(3rd Cheapest) Salume")       .at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("(Very Expensive) Premium").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered("Magic")));

        then(theFirstSuggestionForCustomer(), isThe("(1st Cheapest) Salume").at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat());
        and(secondSuggestedProduct(),         isThe("(2nd Cheapest) Salume").at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat());
        and(thirdSuggestedProduct(),          isThe("(3rd Cheapest) Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat());

        and(numberOfAdvisedProducts(), is(5));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Test
    public void suggestToCustomerWithHealthyProfileTheProductsWithLessFat() throws Exception {
        given(theAvailableProductsAre(
                a("(Super Fat) Salume")   .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("(Lightest) Salume")    .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("(Light) Salume")       .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat(),
                a("(3rd Lightest) Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("(Fat) Premium")    .at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered("Healthy")));

        then(theFirstSuggestionForCustomer(), isThe("(Lightest) Salume")    .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat());
        and(secondSuggestedProduct(),         isThe("(Light) Salume")       .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat());
        and(thirdSuggestedProduct(),          isThe("(3rd Lightest) Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat());

        and(numberOfAdvisedProducts(), is(5));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Notes("Expert clients are more demanding and won't accept anything that is not considered traditional long-honored products.")
    @Test
    public void onlySuggestTraditionalProductsWithCheapestOnesFirstToExperts() throws Exception {
        given(theAvailableProductsAre(
                a("(Normal) Cheap Salume")                   .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("(Normal) Light Salume")                   .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("(Normal) Salume")                         .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat(),
                a("(Traditional Less Expensive) Salume")     .at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("(Traditional More Expensive) Premium").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered("Expert")));

        then(theFirstSuggestionForCustomer(), isThe("(Traditional Less Expensive) Salume") .at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat());
        and(secondSuggestedProduct(),         isThe("(Traditional More Expensive) Premium").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat());

        and(numberOfAdvisedProducts(), is(2));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Test
    public void suggestToCustomerWithGourmetProfileTheMostExpensiveProducts() throws Exception {
        given(theAvailableProductsAre(
                a("(1st Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("(2nd Cheapest) Salume")       .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("(Expensive) & Light")         .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat(),
                a("(3rd More Expensive) Salume") .at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("(Very Expensive) Premium").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered("Gourmet")));

        then(theFirstSuggestionForCustomer(), isThe("(Very Expensive) Premium")   .at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat());
        and(secondSuggestedProduct(),         isThe("(Expensive) & Light")        .at("EUR 57,37").regardedAs("NORMAL")     .with("33,50").percentageOfFat());
        and(thirdSuggestedProduct(),          isThe("(3rd More Expensive) Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat());

        and(numberOfAdvisedProducts(), is(5));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    private GivensBuilder theAvailableProductsAre(ProductBuilder... products) {
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

    private ActionUnderTest requestingBestOfferFor(final String profile) {
        return (givens, capturedInputAndOutputs1) -> {
            // TODO RF 20/10/2015 Extract the server address to a method in the abstract class
            final String advisorUrl = "http://localhost:8081/salume/supplier/advise/for/" + profile;

            this.response = new TestRestTemplate().getForEntity(advisorUrl, String.class);

            capture("Salume advice request", withContent(advisorUrl), from(CUSTOMER), to(SUPPLIER));

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<Integer> numberOfAdvisedProducts() {
        return inputAndOutputs -> ((Double)
                xpath().evaluate("count(//product)", xmlFrom(response.getBody()), NUMBER)
        ).intValue();
    }

    private StateExtractor<Node> theFirstSuggestionForCustomer() throws Exception {
        capture("Salume advice response", withContent(prettyPrint(xmlFrom(response.getBody()))), from(SUPPLIER), to(CUSTOMER));

        return firstSuggestedProduct();
    }

    private StateExtractor<Node> firstSuggestedProduct() {
        return inputAndOutputs -> ((NodeList)
                xpath().evaluate("//product", xmlFrom(response.getBody()), NODESET)
        ).item(0);
    }

    private StateExtractor<Node> secondSuggestedProduct() {
        return inputAndOutputs -> ((NodeList)
                xpath().evaluate("//product", xmlFrom(response.getBody()), NODESET)
        ).item(1);
    }

    private StateExtractor<Node> thirdSuggestedProduct() {
        return inputAndOutputs -> ((NodeList)
                xpath().evaluate("//product", xmlFrom(response.getBody()), NODESET)
        ).item(2);
    }

    //
    // Decorator methods to make the test read well
    //

    private String aCustomerConsidered(String profile) {
        return profile;
    }

    private ProductBuilder and(ProductBuilder p) {
        return p;
    }

    //
    // Matchers
    //

    private <T> Matcher<T> is(T expected) {
        return Matchers.is(expected);
    }

    private AdvisedProductMatcherBuilder isThe(String productName) {
        return AdvisedProductMatcherBuilder.isThe(moneyDealer, productName);
    }

    static class AdvisedProductMatcherBuilder extends AbstractAdvisedProductMatcherBuilder<Node> {

        static AdvisedProductMatcherBuilder isThe(MoneyDealer moneyDealer, String expectedProduct) {
            return new AdvisedProductMatcherBuilder(moneyDealer, expectedProduct);
        }

        private AdvisedProductMatcherBuilder(MoneyDealer moneyDealer, String expectedProductName) {
            super(moneyDealer, expectedProductName);
        }

        @Override
        public AdvisedProductMatcher percentageOfFat() {
            final Product product = expectedProduct();
            return new AdvisedProductMatcher(
                    product.getName(),
                    moneyDealer().format(product.getPrice()),
                    ReputationRepresentation.of(product.getReputation()),
                    product.getFatPercentage());
        }
    }

    // TODO RF 07/10 I'm already triplicating this method...
    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> response.getHeaders().getContentType();
    }

}
