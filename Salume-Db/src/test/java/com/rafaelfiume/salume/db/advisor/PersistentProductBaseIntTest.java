package com.rafaelfiume.salume.db.advisor;

import com.rafaelfiume.salume.db.DbApplication;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.ProductBuilder;
import com.rafaelfiume.salume.domain.matchers.AbstractAdvisedProductMatcherBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

import static com.rafaelfiume.salume.domain.ProductBuilder.a;
import static java.lang.String.format;
import static org.junit.Assert.assertThat;
import static org.springframework.test.jdbc.JdbcTestUtils.deleteFromTables;

@ContextConfiguration(classes = DbApplication.class)
@Transactional
public class PersistentProductBaseIntTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private List<Product> actualSuggestions;

    private JdbcTemplate jdbcTemplate;

    private MoneyDealer moneyDealer;

    private PersistentProductBase underTest;

    @Autowired
    @SuppressWarnings("unused")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Autowired
    @SuppressWarnings("unused")
    public void setMoneyDealer(MoneyDealer moneyDealer) {
        this.moneyDealer = moneyDealer;
    }

    @Autowired
    @SuppressWarnings("unused")
    public void setAdvisorDao(PersistentProductBase advisorDao) {
        this.underTest = advisorDao;
    }

    @Test
    public void shouldReturnTheCheapestProductsForCustomerWithMagicProfile() {
        add(a("(Second Cheapest) Salume").at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat(),
            a("(Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat(),
            a("(Expensive) & Light")     .at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());

        actualSuggestions = underTest.productsForMagic();

        assertThat(firstSuggestion(),  isThe("(Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat());
        assertThat(secondSuggestion(), isThe("(Second Cheapest) Salume").at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat());
        assertThat(thirdSuggestion(),  isThe("(Expensive) & Light")     .at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());
    }

    @Test
    public void shouldReturnTheProductsWithLessFatForCustomerWithHealthyProfile() {
        add(a("(Lightest) Salume")       .at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat(),
            a("(Fattest) Salume")        .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat(),
            a("(Second Lightest) Salume").at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());

        actualSuggestions = underTest.productsForHealthy();

        assertThat(firstSuggestion(),  isThe("(Lightest) Salume")       .at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat());
        assertThat(secondSuggestion(), isThe("(Second Lightest) Salume").at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());
        assertThat(thirdSuggestion(),  isThe("(Fattest) Salume")        .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat());
    }

    @Test
    public void shouldReturnOnlyTraditionalProductsWithCheapestOnesFirstForCustomerWithExpertProfile() {
        add(a("(Traditional) Salume")               .at("EUR 29,55").regardedAs("TRADITIONAL").with("31,00").percentageOfFat(),
            a("(Normal) Cheapest Salume")           .at("EUR 11,11").regardedAs("NORMAL")     .with("49,99").percentageOfFat(),
            a("(Traditional) More Expensive Salume").at("EUR 57,37").regardedAs("TRADITIONAL").with("37,55").percentageOfFat());

        actualSuggestions = underTest.productsForExpert();

        assertThat(firstSuggestion(),  isThe("(Traditional) Salume")               .at("EUR 29,55").regardedAs("TRADITIONAL").with("31,00").percentageOfFat());
        assertThat(secondSuggestion(), isThe("(Traditional) More Expensive Salume").at("EUR 57,37").regardedAs("TRADITIONAL").with("37,55").percentageOfFat());

        assertThat(actualSuggestions, hasOnlyTraditionalProducts());
    }

    @Test
    public void shouldReturnMostExpendiveOnesFirstForCustomerWithExpertProfile() {
        add(a("(Second Cheapest) Salume").at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat(),
                a("(Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat(),
                a("(Expensive) & Light")     .at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());

        actualSuggestions = underTest.productsForGourmet();

        assertThat(firstSuggestion(),  isThe("(Expensive) & Light")     .at("EUR 57,37").regardedAs("NORMAL").with("37,55").percentageOfFat());
        assertThat(secondSuggestion(), isThe("(Second Cheapest) Salume").at("EUR 29,55").regardedAs("NORMAL").with("31,00").percentageOfFat());
        assertThat(thirdSuggestion(),  isThe("(Cheapest) Salume")       .at("EUR 11,11").regardedAs("NORMAL").with("49,99").percentageOfFat());
    }

    private Matcher<? super List<Product>> hasOnlyTraditionalProducts() {
        return Matchers.hasSize(2);
    }

    private Product firstSuggestion() {
        return actualSuggestions.get(0);
    }

    private Product secondSuggestion() {
        return actualSuggestions.get(1);
    }

    private Product thirdSuggestion() {
        return actualSuggestions.get(2);
    }

    private void add(ProductBuilder... products) {
        deleteFromTables(jdbcTemplate, "salumistore.products");
        for (ProductBuilder p : products) {
            underTest.add(p.build(moneyDealer));
        }
    }

    //
    //// Matchers
    //

    private AdvisedProductMatcherBuilder isThe(String productName) {
        return AdvisedProductMatcherBuilder.isThe(moneyDealer, productName);
    }

    static class AdvisedProductMatcherBuilder extends AbstractAdvisedProductMatcherBuilder<Product> {

        static AdvisedProductMatcherBuilder isThe(MoneyDealer moneyDealer, String expectedProduct) {
            return new AdvisedProductMatcherBuilder(moneyDealer, expectedProduct);
        }

        private AdvisedProductMatcherBuilder(MoneyDealer moneyDealer, String expectedProductName) {
            super(moneyDealer, expectedProductName);
        }

        @Override
        public ProductMather percentageOfFat() {
            return new ProductMather(expectedProduct());
        }
    }

    static class ProductMather extends TypeSafeMatcher<Product> {

        private final Product expected;

        ProductMather(Product expected) {
            this.expected = expected;
        }

        @Override
        protected boolean matchesSafely(Product actual) {
            return expected.getName().equals(actual.getName())
                    && expected.getPrice().equals(actual.getPrice())
                    && expected.getReputation() == actual.getReputation()
                    && expected.getFatPercentage().equals(actual.getFatPercentage());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(format(
                    "a product with name \"%s\", price \"%s\", reputation \"%s\", and fat percentage \"%s\"",
                    expected.getName(), expected.getPrice(), expected.getReputation(), expected.getFatPercentage())
            );
        }

        @Override
        protected void describeMismatchSafely(Product actual, Description mismatchDescription) {
            mismatchDescription.appendText(format(
                    "product had name \"%s\", price \"%s\", reputation \"%s\", and fat percentage \"%s\"",
                    actual.getName(), actual.getPrice(), actual.getReputation(), actual.getFatPercentage()));
        }
    }
}
