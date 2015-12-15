package com.rafaelfiume.salume.acceptance.advisor;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.ProductBuilder;
import com.rafaelfiume.salume.db.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.support.TestSetupException;
import com.rafaelfiume.salume.support.transactions.SpringCommitsAndClosesTestTransactionTransactor;
import com.rafaelfiume.salume.web.result.ReputationRepresentation;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
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
import javax.xml.xpath.XPathExpressionException;
import java.text.NumberFormat;
import java.text.ParseException;

import static com.rafaelfiume.salume.ProductBuilder.a;
import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static com.rafaelfiume.salume.support.Applications.CUSTOMER;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static com.rafaelfiume.salume.support.Xml.*;
import static java.lang.String.format;
import static java.util.Locale.ITALY;
import static javax.xml.xpath.XPathConstants.*;
import static org.springframework.http.MediaType.parseMediaType;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "See an explanation about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
@Transactional
public class SalumeAdvisorHappyPathEndToEndTest extends AbstractSequenceDiagramTestState {

    private static final MediaType APPLICATION_XML_CHARSET_UTF8 = parseMediaType("application/xml;charset=utf-8");

    private final SpringCommitsAndClosesTestTransactionTransactor transactor = new SpringCommitsAndClosesTestTransactionTransactor();

    private MoneyDealer moneyDealer;

    private ResponseEntity<String> response;

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcDatabaseSupport db;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    public void setSimpleJdbcDatabaseSupport(SimpleJdbcDatabaseSupport db) {
        this.db = db;
    }

    @Autowired
    public void setMoneyDealer(MoneyDealer moneyDealer) {
        this.moneyDealer = moneyDealer;
    }

    @Notes("Gioseppo select the customer profile when serving his customers.\n\n" +
            "" +
            "After showing a previous version of the acceptance test and having some conversation, it was clear that the\n" +
            "result should be a list of (3) products instead of a single one.\n\n" +
            "" +
            "Special is another word for \"ordinary non tradition product\".")
    @Table({
            @Row({"Magic"  , "Cheap Salume"     , "EUR 11,11", "49,99", "NORMAL"}),
            @Row({"Healthy", "Expensive & Light", "EUR 57,37", "31,00", "NORMAL"}),
            @Row({"Gourmet", "Premium Salume"   , "EUR 73,23", "38,00", "TRADITIONAL"})
    })
    @Test
    public void suggestUpToThreeDifferentProductsAccordingToClientProfile(String profile, String product, String price, String fatPercentage, String productReputation) throws Exception {
        given(theAvailableProductsAre(
                a("Cheap Salume")      .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("Light Salume")      .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("Expensive & Light") .at("EUR 57,37").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("Traditional Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("Premium Salume").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered(profile)));

        then(theFirstSuggestionForCustomer(), isThe(product).at(price).regardedAs(productReputation).with(fatPercentage).percentageOfFat());
        and(numberOfAdvisedProducts(), is(3));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Notes("Expert clients are more demanding and won't accept anything that is not considered traditional long-honored products.")
    @Test
    public void onlySuggestTraditionalProductsToExperts() throws Exception {
        given(theAvailableProductsAre(
                a("Cheap Salume")      .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
                a("Light Salume")      .at("EUR 29,55").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("Expensive & Light") .at("EUR 57,37").regardedAs("NORMAL")     .with("31,00").percentageOfFat(),
                a("Traditional Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat(),
                and(a("Premium Salume").at("EUR 73,23").regardedAs("TRADITIONAL").with("38,00").percentageOfFat())));

        when(requestingBestOfferFor(aCustomerConsidered("Expert")));

        then(theFirstSuggestionForCustomer(), isThe("Traditional Salume").at("EUR 41,60").regardedAs("TRADITIONAL").with("37,00").percentageOfFat());
        and(numberOfAdvisedProducts(), is(2));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    private GivensBuilder theAvailableProductsAre(ProductBuilder... products) {
        return givens -> {
            transactor.perform(() -> {
                deleteFromTables(jdbcTemplate, "salumistore.products");

                long id = 1;
                for (ProductBuilder p : products) {
                    addProduct(p.build(moneyDealer), id);
                    id++;
                }
            });

            return givens;
        };
    }

    private static final NumberFormat FAT_FORMATTER = NumberFormat.getNumberInstance(ITALY);
    static {
        FAT_FORMATTER.setMinimumFractionDigits(2);
    }
    private void addProduct(Product p, long id) {
        try {
            db.execute(
                    format("INSERT INTO salumistore.products VALUES (%s, '%s', %s, %s, %s);",
                            id,
                            p.getName(),
                            p.getPrice().getNumber(),
                            FAT_FORMATTER.parse(p.getFatPercentage()),
                            (p.getReputation() == NORMAL) ? 2 : 1 // TODO RF 13/12 Blerghhh But focusing on making the test readable first
                    )
            );
        } catch (ParseException e) {
            throw new RuntimeException("this ugly exception handling is going to disappear in the next few commits...", e);
        }
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

    //
    // Decorator methods to make the test read well
    //

    private String aCustomerConsidered(String profile) {
        return profile;
    }

    private ProductBuilder and(ProductBuilder p) {
        return p;
    }

    private AdvisedProductMatcherBuilder isThe(String productName) {
        return AdvisedProductMatcherBuilder.isThe(moneyDealer, productName);
    }

    //
    // Matchers
    //

    private <T> Matcher<T> is(T expected) {
        return Matchers.is(expected);
    }

    static class AdvisedProductMatcherBuilder {

        private final ProductBuilder expectedProduct;
        private final MoneyDealer moneyDealer;

        static AdvisedProductMatcherBuilder isThe(MoneyDealer moneyDealer, String expectedProduct) {
            return new AdvisedProductMatcherBuilder(moneyDealer, expectedProduct);
        }

        AdvisedProductMatcherBuilder(MoneyDealer moneyDealer, String expectedProductName) {
            this.moneyDealer = moneyDealer;
            this.expectedProduct = new ProductBuilder(expectedProductName);
        }

        AdvisedProductMatcherBuilder at(String price) {
            this.expectedProduct.at(price);
            return this;
        }

        AdvisedProductMatcherBuilder regardedAs(String reputation) {
            this.expectedProduct.regardedAs(reputation);
            return this;
        }

        AdvisedProductMatcherBuilder with(String fat) {
            this.expectedProduct.with(fat);
            return this;
        }

        AdvisedProductMatcher percentageOfFat() {
            // Just make the test read nicer. Use it after # to build the matcher
            final Product product = expectedProduct.build(moneyDealer);
            return new AdvisedProductMatcher(
                    product.getName(),
                    moneyDealer.format(product.getPrice()),
                    ReputationRepresentation.of(product.getReputation()),
                    product.getFatPercentage());
        }
    }

    static class AdvisedProductMatcher extends TypeSafeMatcher<Node> {

        private final String expectedName;
        private final String expectedPrice;
        private final String expectedReputation;
        private final String expectedFatPercentage;

        AdvisedProductMatcher(String expectedName, String expectedPrice, String expectedReputation, String expectedFatPercentage) {
            this.expectedName = expectedName;
            this.expectedPrice = expectedPrice;
            this.expectedReputation = expectedReputation;
            this.expectedFatPercentage = expectedFatPercentage;
        }

        @Override
        protected boolean matchesSafely(Node xml) {
            return expectedName.equals(actualNameFrom(xml))
                    && expectedPrice.equals(actualPriceFrom(xml))
                    && expectedReputation.equals(actualReputationFrom(xml))
                    && expectedFatPercentage.equals(actualFatPercentageFrom(xml));
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(format(
                    "a product with name \"%s\", price \"%s\", reputation \"%s\", and fat percentage \"%s\"",
                    expectedName, expectedPrice, expectedReputation, expectedFatPercentage)
            );
        }

        @Override
        protected void describeMismatchSafely(Node xml, Description mismatchDescription) {
            mismatchDescription.appendText(format(
                    "product had name \"%s\", price \"%s\", reputation \"%s\", and fat percentage \"%s\"",
                    actualNameFrom(xml), actualPriceFrom(xml), actualReputationFrom(xml), actualFatPercentageFrom(xml)));
        }

        private String actualNameFrom(Node xml) {          return getValueFrom(xml, "name"); }
        private String actualPriceFrom(Node xml) {         return getValueFrom(xml, "price"); }
        private String actualReputationFrom(Node xml) {    return getValueFrom(xml, "reputation"); }
        private String actualFatPercentageFrom(Node xml) { return getValueFrom(xml, "fat-percentage"); }

        private String getValueFrom(Node item, String xpath) {
            try {
                return (String) xpath().evaluate(xpath + "/text()", item, STRING);
            } catch (XPathExpressionException e) {
                throw new TestSetupException(e);
            }
        }
    }

    // TODO RF 07/10 I'm already triplicating this method...
    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> response.getHeaders().getContentType();
    }

}
