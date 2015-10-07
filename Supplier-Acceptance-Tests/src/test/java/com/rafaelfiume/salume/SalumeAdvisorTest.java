package com.rafaelfiume.salume;

import com.googlecode.yatspec.junit.Notes;
import com.googlecode.yatspec.junit.Row;
import com.googlecode.yatspec.junit.Table;
import com.googlecode.yatspec.state.givenwhenthen.ActionUnderTest;
import com.googlecode.yatspec.state.givenwhenthen.GivensBuilder;
import com.googlecode.yatspec.state.givenwhenthen.StateExtractor;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.parseMediaType;

@Ignore
@Notes("A customer can have whatever they want as long as it is salume. At least for now...\n\n" +
        "See an explanation about this story <a href=\"https://rafaelfiume.wordpress.com/2013/04/07/dragons-unicorns-and-titans-an-agile-software-developer-tail/\" target=\"blank\">here</a>.")
public class SalumeAdvisorTest extends AbstractSequenceDiagramTestState {

    private static final MediaType APPLICATION_JSON_CHARSET_UTF8 = parseMediaType("application/json;charset=utf-8");

    private ResponseEntity<String> response;

    @Notes("It's up to Gioseppo to select the customer profile based when serving his customers")
    @Test
    @Table({
            @Row({"magic"  , "cheap"}),
            @Row({"healthy", "light"}),
            @Row({"expert" , "traditional"}),
            @Row({"gourmet", "premium"})
    })
    public void adviceSalume(String profile, String product) throws Exception {
        given(availableProductsAre(cheap(), light(), traditional(), andPremium()));

        when(requestingBestOfferFor(aCustomerConsidered(profile)));

        then(adviseCustomerTo(), buy(product, salume()));
        then(theContentType(), is(APPLICATION_JSON_CHARSET_UTF8));
    }

    private GivensBuilder availableProductsAre(String... products) {
        return givens -> {
            // DatabaseUtilities.cleanAll();
            // DatabaseUtilities.addProducts(products);

            return givens;
        };
    }

    private ActionUnderTest requestingBestOfferFor(final String profile) {
        return (givens, capturedInputAndOutputs1) -> {
            String advisorUrl = "http://localhost:8080/salume/supplier/advise/for/" + profile;

            this.response = new TestRestTemplate().getForEntity(advisorUrl, String.class);

            capturedInputAndOutputs.add("Salume advice request from customer to Supplier", advisorUrl);

            return capturedInputAndOutputs;
        };
    }

    private StateExtractor<String> adviseCustomerTo() {
        return inputAndOutputs -> {
            String responseBody = response.getBody();

            capturedInputAndOutputs.add("Salume advice response from Supplier to customer", responseBody);

            return responseBody;
        };
    }

    // TODO RF 07/10 I'm already triplicating this method...
    private StateExtractor<MediaType> theContentType() {
        return inputAndOutputs -> response.getHeaders().getContentType();
    }

    private String aCustomerConsidered(String profile) {
        return profile;
    }

    private String salume() {
        return "";
    }

    String cheap() {
        return "cheap";
    }

    String light() {
        return "light";
    }

    String traditional() {
        return "traditional";
    }

    String andPremium() {
        return "premium";
    }

    private Matcher<? super String> buy(String expected, @SuppressWarnings("unused") String salume) {
        return new TypeSafeMatcher<String>() {
            @Override
            protected boolean matchesSafely(String result) {
                return expected.equals(result);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(expected);
            }
        };
    }

}
