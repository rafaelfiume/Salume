package com.rafaelfiume.salume.matchers;

import com.rafaelfiume.salume.support.TestSetupException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

import static com.rafaelfiume.salume.support.Xml.xpath;
import static java.lang.String.format;
import static javax.xml.xpath.XPathConstants.STRING;

public class AdvisedProductMatcher extends TypeSafeMatcher<Node> {

    private final String expectedName;
    private final String expectedPrice;
    private final String expectedReputation;
    private final String expectedFatPercentage;

    public AdvisedProductMatcher(String expectedName, String expectedPrice, String expectedReputation, String expectedFatPercentage) {
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
