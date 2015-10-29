package com.rafaelfiume.salume.advisor;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.support.AbstractSequenceDiagramTestState;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.money.MonetaryAmount;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static com.rafaelfiume.salume.domain.Product.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Product.Reputation.TRADITIONAL;
import static com.rafaelfiume.salume.support.Applications.CUSTOMER;
import static com.rafaelfiume.salume.support.Applications.SUPPLIER;
import static javax.xml.xpath.XPathConstants.NODESET;
import static javax.xml.xpath.XPathConstants.NUMBER;
import static org.hamcrest.Matchers.hasXPath;
import static org.springframework.http.MediaType.parseMediaType;

@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "See an explanation about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
public class SalumeAdvisorHappyPathTest extends AbstractSequenceDiagramTestState {

    private static final MediaType APPLICATION_XML_CHARSET_UTF8 = parseMediaType("application/xml;charset=utf-8");

    private ResponseEntity<String> response;

    @Notes("Gioseppo select the customer profile when serving his customers.\n\n" +
            "" +
            "After showing a previous version of the acceptance test and having some conversation, it was clear that the\n" +
            "result should be a list of (3) products instead of a single one.\n\n" +
            "" +
            "Special is another word for \"ordinary non tradition product\".")
    @Test
    @Table({
            @Row({"Magic", "Cheap Salume", "EUR 15,55", "49,99", "special"}),
            @Row({"Healthy", "Not Light In Your Pocket", "EUR 57,37", "31,00", "special"}),
            @Row({"Gourmet", "Premium Salume", "EUR 73,23", "38,00", "traditional"})
    })
    public void suggestUpToThreeChoicesAccordingToClientProfile(String profile, String product, String price, String fatPercentage, String traditional) throws Exception {
        given(availableProductsAre(cheap(), light(), traditional(), andPremium()));

        when(requestingBestOfferFor(aCustomerConsidered(profile)));

        then(numberOfAdvisedProducts(), is(3));
        then(adviseCustomerTo(), buy(product, salume()));
        then(firstSuggestedProduct(), hasPrice(price));
        then(firstSuggestedProduct(), hasFatPercentage(fatPercentage));
        then(firstSuggestedProduct(), isRegardedAs(traditional));
        then(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    @Notes("Expert clients are more demanding and won't accept anything that is not considered traditional long-honored products.")
    @Test
    public void onlySuggestTraditionalProductsToExperts() throws Exception {
        given(onlyTwoOfTheAvailableProductsAreTraditional());

        when(requestingBestOfferFor(aCustomerConsidered("Expert")));

        then(numberOfAdvisedProducts(), is(2));

        then(adviseCustomerTo(), buy("Traditional Salume", salume()));
        then(firstSuggestedProduct(), hasPrice("EUR 41,60"));
        then(firstSuggestedProduct(), hasFatPercentage("37,00"));
        then(firstSuggestedProduct(), isRegardedAs("traditional"));

        then(secondSuggestedProduct(), is("Premium Salume", salume()));
        then(secondSuggestedProduct(), isRegardedAs("traditional"));

        then(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    private GivensBuilder availableProductsAre(Product... products) {
        return givens -> {
            // Data added using sql scripts. See: 01.create-table.sql

            // Consider doing something like the following in the future...
            // DatabaseUtilities.cleanAll();
            // DatabaseUtilities.addProducts(products);

            return givens;
        };
    }

    private GivensBuilder onlyTwoOfTheAvailableProductsAreTraditional() {
        return givens -> {
            // Data added using sql scripts. See: 01.create-table.sql

            // Consider doing something like the following in the future...
            // DatabaseUtilities.cleanAll();
            // DatabaseUtilities.addProducts(products);

            return givens;
        };
    }

    private ActionUnderTest requestingBestOfferFor(final String profile) {
        return (givens, capturedInputAndOutputs1) -> {
            // TODO RF 20/10/2015 Extract the server address to a method in the abstract class
            String advisorUrl = "http://localhost:8081/salume/supplier/advise/for/" + profile;

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

    private StateExtractor<Node> adviseCustomerTo() throws Exception {
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

    Product cheap() {
        return new Product(1L, "Cheap Salume", moneyOf(15.55), "49,99", NORMAL);
    }

    Product light() {
        return new Product(2L, "Light Salume", moneyOf(29.55), "31,00", NORMAL);
    }

    Product traditional() {
        return new Product(3L, "Traditional Salume", moneyOf(41.60), "37,00", TRADITIONAL);
    }

    Product andPremium() {
        return new Product(4L, "Premium Salume", moneyOf(73.23), "38,00", TRADITIONAL);
    }

    //
    // Matchers
    //

    private Matcher<Node> buy(String expected, @SuppressWarnings("unused") String salume) {
        return hasXPath("//product/name[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> hasPrice(String expected) {
        return hasXPath("//product/price[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> hasFatPercentage(String expected) {
        return hasXPath("//product/fat-percentage[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> isRegardedAs(String expected) {
        return hasXPath("//product/reputation[text() = \"" + expected + "\"]");
    }

    private <T> Matcher<T> is(T expected) {
        return Matchers.is(expected);
    }

    private Matcher<Node> is(String expected, @SuppressWarnings("unused") String salume) {
        return hasXPath("//product/name[text() = \"" + expected + "\"]");
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

    private String salume() {
        return "";
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

}
