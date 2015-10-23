package com.rafaelfiume.salume;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import com.rafaelfiume.salume.domain.Product;
import org.hamcrest.Matcher;
import org.javamoney.moneta.Money;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.money.MonetaryAmount;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static com.rafaelfiume.salume.domain.Product.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Product.Reputation.TRADITIONAL;
import static org.hamcrest.Matchers.hasXPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.parseMediaType;

@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "See an explanation about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
public class SalumeAdvisorTest extends AbstractSequenceDiagramTestState {

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
            @Row({"Magic"  , "Cheap Salume"       , "EUR 15,55", "49,99", "special"})
//            @Row({"Healthy", "Light Salume"       , "EUR 29,55", "31,00", "special"}),
//            @Row({"Expert" , "Traditional Salume" , "EUR 41,60", "37,00", "traditional"}),
//            @Row({"Gourmet", "Premium Salume"     , "EUR 73,23", "38,00", "traditional"})
    })
    public void adviceSalume(String profile, String product, String price, String fatPercentage, String traditional) throws Exception {
        given(availableProductsAre(cheap(), light(), traditional(), andPremium()));

        when(requestingBestOfferFor(aCustomerConsidered(profile)));

        then(numberOfAdvisedProducts(), is(3));
        then(adviseCustomerTo(), buy(product, salume()));
        then(firstSuggestedProduct(), hasPrice(price));
        then(firstSuggestedProduct(), hasFatPercentage(fatPercentage));
        then(firstSuggestedProduct(), isRegardedAs(traditional));
        then(theContentType(), is(APPLICATION_XML_CHARSET_UTF8));
    }

    private GivensBuilder availableProductsAre(Product... products) {
        return givens -> {
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

            // TODO RF 20/10/2015 Extract it to a method in the abstract class
            capturedInputAndOutputs.add("Salume advice request from customer to Supplier", advisorUrl);

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<Integer> numberOfAdvisedProducts() {
        return inputAndOutputs -> {
            final Document xml = toDocument(response.getBody());

            // TODO RF 20/10/2015 Extract it to a method in the abstract class
            capturedInputAndOutputs.add("Salume advice response from Supplier to customer", prettyPrint(xml));

            return ((Double) xpath().evaluate("count(//product)", xml, XPathConstants.NUMBER)).intValue();
        };
    }

    private StateExtractor<Node> adviseCustomerTo() {
        return inputAndOutputs -> {
            final Document xml = toDocument(response.getBody());

            // TODO RF 20/10/2015 Extract it to a method in the abstract class
            capturedInputAndOutputs.add("Salume advice response from Supplier to customer", prettyPrint(xml));

            return xml;
        };
    }

    private StateExtractor<Node> firstSuggestedProduct() {
        return inputAndOutputs -> toDocument(response.getBody());
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
        return hasXPath("//product[1]/name[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> hasPrice(String expected) {
        return hasXPath("//product[1]/price[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> hasFatPercentage(String expected) {
        return hasXPath("//product[1]/fat-percentage[text() = \"" + expected + "\"]");
    }

    private Matcher<Node> isRegardedAs(String expected) {
        return hasXPath("//product[1]/reputation[text() = \"" + expected + "\"]");
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

    private static Document toDocument(String xml) throws Exception {
        Document xmlDoc= DocumentBuilderFactory
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
