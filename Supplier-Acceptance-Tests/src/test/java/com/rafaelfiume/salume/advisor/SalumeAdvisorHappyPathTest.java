package com.rafaelfiume.salume.advisor;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.googlecode.yatspec.state.givenwhenthen.TestState;
import com.rafaelfiume.salume.db.SimpleJdbcDatabaseSupport;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.Reputation;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import com.rafaelfiume.salume.support.TestSetupException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.transaction.TestTransaction;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static com.rafaelfiume.salume.support.Applications.CUSTOMER;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static java.lang.String.format;
import static java.util.Locale.ITALY;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.NUMBER;
import static javax.xml.xpath.XPathConstants.STRING;
import static org.hamcrest.Matchers.hasXPath;
import static org.springframework.http.MediaType.parseMediaType;

@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "See an explanation about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
@Transactional
public class SalumeAdvisorHappyPathTest extends AbstractSequenceDiagramTestState {

    private static final MediaType APPLICATION_XML_CHARSET_UTF8 = parseMediaType("application/xml;charset=utf-8");

    private ResponseEntity<String> response;

    private final JdbcTestUtils dbUtils = new JdbcTestUtils();

    private final SpringCommitsAndClosesTestTransactionTransactor transactor = new SpringCommitsAndClosesTestTransactionTransactor();

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
                a("Cheap Salume"      , at("EUR 11,11"), regardedAs("NORMAL")     , with("49,99"), percentageOfFat()),
                a("Light Salume"      , at("EUR 29,55"), regardedAs("NORMAL")     , with("31,00"), percentageOfFat()),
                a("Expensive & Light" , at("EUR 57,37"), regardedAs("NORMAL")     , with("31,00"), percentageOfFat()),
                a("Traditional Salume", at("EUR 41,60"), regardedAs("TRADITIONAL"), with("37,00"), percentageOfFat()),
                and(a("Premium Salume", at("EUR 73,23"), regardedAs("TRADITIONAL"), with("38,00"), percentageOfFat()))));

        when(requestingBestOfferFor(aCustomerConsidered(profile)));

        then(theFirstSuggestionForCustomer(), isThe(product, at(price), regardedAs(productReputation), with(fatPercentage), percentageOfFat()));
        and(numberOfAdvisedProducts(), is(3));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Notes("Expert clients are more demanding and won't accept anything that is not considered traditional long-honored products.")
    @Test
    public void onlySuggestTraditionalProductsToExperts() throws Exception {
        given(theAvailableProductsAre(
                a("Cheap Salume"      , at("EUR 11,11"), regardedAs("NORMAL"),      with("49,99"), percentageOfFat()),
                a("Light Salume"      , at("EUR 29,55"), regardedAs("NORMAL"),      with("31,00"), percentageOfFat()),
                a("Expensive & Light" , at("EUR 57,37"), regardedAs("NORMAL"),      with("31,00"), percentageOfFat()),
                a("Traditional Salume", at("EUR 41,60"), regardedAs("TRADITIONAL"), with("37,00"), percentageOfFat()),
                and(a("Premium Salume", at("EUR 73,23"), regardedAs("TRADITIONAL"), with("38,00"), percentageOfFat()))));

        when(requestingBestOfferFor(aCustomerConsidered("Expert")));

        then(theFirstSuggestionForCustomer(), isThe("Traditional Salume", at("EUR 41,60"), regardedAs("TRADITIONAL"), with("37,00"), percentageOfFat()));
        and(numberOfAdvisedProducts(), is(2));
        and(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    private GivensBuilder theAvailableProductsAre(Product... products) {
        return givens -> {
            transactor.perform(() -> {
                dbUtils.deleteFromTables(jdbcTemplate, "salumistore.products");

                long id = 1;
                for (Product p : products) {
                    addProduct(p, id);
                    id++;
                }
            });

            return givens;
        };
    }

    private static final NumberFormat FAT_FORMATTER = NumberFormat.getNumberInstance(ITALY);
    {
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

    private Product a(String name, String price, Reputation reputation, String fatPercentage, @SuppressWarnings("unused") Void aVoid) {
        return new Product(1L, name, formatoPadrao.parse(price), fatPercentage, reputation);
    }

    private final  MonetaryAmountFormat formatoPadrao = MonetaryFormats.getAmountFormat(Locale.ITALY);

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
    // Matchers
    //

    private <T> Matcher<T> is(T expected) {
        return Matchers.is(expected);
    }

    private TypeSafeMatcher<Node> isThe(final String expectedProduct, final String expectedPrice, final Reputation expectedReputation, final String expectedFatPercentage, @SuppressWarnings("unused") Void aVoid) {
        return new TypeSafeMatcher<Node>() {

            @Override
            protected boolean matchesSafely(Node xml) {
                final String reputation = (NORMAL == expectedReputation) ? "special" : "traditional"; // This logic is out of place... Move it to ProductResponse and test it

                return expectedProduct.equals(actualNameFrom(xml) )
                        && expectedPrice.equals(actualPriceFrom(xml))
                        && reputation.equals(actualReputationFrom(xml))
                        && expectedFatPercentage.equals(actualFatPercentageFrom(xml));
            }

            @Override
            public void describeTo(Description description) {
                System.out.println("expectedReputation: " + expectedReputation);
                final String reputation = ("NORMAL".equals(expectedReputation)) ? "special" : "traditional"; // This logic is out of place... Move it to ProductResponse and test it

                description.appendText(format(
                        "a product with name \"%s\", price \"%s\", reputation \"%s\", and fat percentage \"%s\"",
                        expectedProduct, expectedPrice, reputation, expectedFatPercentage)
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
        };
    }

    // TODO RF 07/10 I'm already triplicating this method...
    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> response.getHeaders().getContentType();
    }

    private MonetaryAmount moneyOf(Number money) {
        return Money.of(money, "EUR");
    }

    //
    // Decorator methods to make the test read well
    //

    private String aCustomerConsidered(String profile) {
        return profile;
    }

    private String at(String price) {
        return price;
    }

    private Reputation regardedAs(String reputation) {
        return Reputation.valueOf(reputation);
    }

    private String with(String fatPercentage) {
        return fatPercentage;
    }

    private Void percentageOfFat() {
        // just making the test read nicer
        return null;
    }

    private Product and(Product p) {
        return p;
    }

    private <ItemOfInterest> TestState and(StateExtractor<ItemOfInterest> extractor, Matcher<? super ItemOfInterest> matcher) throws Exception {
        return then(extractor, matcher);
    }

    //
    // Xml related
    //

    private static Document xmlFrom(String xml) throws Exception {
        Document xmlDoc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new InputSource(new StringReader(xml)));

        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(xmlDoc), new DOMResult());
        return xmlDoc;
    }

    private static String prettyPrint(Node xml) throws Exception {
        final Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        final Writer out = new StringWriter();
        transformer.transform(new DOMSource(xml), new StreamResult(out));
        return out.toString();
    }

    private XPath xpath() {
        return XPathFactory.newInstance().newXPath();
    }

    //
    //// Transaction Management stuff
    //

    public interface UnitOfWork {

        void work() throws Exception;
    }

    public static class SpringCommitsAndClosesTestTransactionTransactor {

        public void perform(UnitOfWork unitOfWork) throws Exception {
            // Spring starts transaction...

            unitOfWork.work();  // Do the job

            // Now commit stuff
            TestTransaction.flagForCommit();
            TestTransaction.end();
        }
    }

}
