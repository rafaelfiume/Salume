package com.rafaelfiume.salume.db.advisor;

import com.rafaelfiume.salume.db.DbApplication;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.transaction.annotation.Transactional;

import javax.money.MonetaryAmount;
import java.text.ParseException;
import java.util.List;

import static com.rafaelfiume.salume.domain.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Reputation.TRADITIONAL;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@ContextConfiguration(classes = DbApplication.class)
@Transactional
@Sql("clean-products.sql")
@Sql("populate-products.sql")
public class PersistentProductBaseIntTest {

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MoneyDealer moneyDealer;

    private PersistentProductBase underTest;

    @Autowired
    public void setAdvisorDao(PersistentProductBase advisorDao) {
        this.underTest = advisorDao;
    }

    @Autowired
    public void setMoneyDealer(MoneyDealer moneyDealer) {
        this.moneyDealer = moneyDealer;
    }

    @Test
    public void magicProfile1() throws ParseException {
        List<Product> suggested = underTest.productsForMagic();

        assertThat(suggested, hasSize(3));

        assertThat(first(suggested).getId(), is(1L));
        assertThat(first(suggested).getName(), is("Cheap Salume"));
        assertThat(first(suggested).getPrice(), is(theAmountOf("EUR 15,55")));
        assertThat(first(suggested).getFatPercentage(), is("49,99"));
        assertThat(first(suggested).getReputation(), is(NORMAL));

        assertThat(second(suggested).getName(), is("Light Salume"));
        assertThat(second(suggested).getPrice(), is(theAmountOf("EUR 29,55")));

        assertThat(third(suggested).getName(), is("Traditional Salume"));
        assertThat(third(suggested).getPrice(), is(theAmountOf("EUR 41,60")));
    }
    @Test
    public void healthyProfile() throws ParseException {
        List<Product> suggested = underTest.productsForHealthy();

        assertThat(suggested, hasSize(3));

        assertThat(first(suggested).getId(), is(3L));
        assertThat(first(suggested).getName(), is("Not Light In Your Pocket"));
        assertThat(first(suggested).getPrice(), is(theAmountOf("EUR 57,37")));
        assertThat(first(suggested).getFatPercentage(), is("31,00"));
        assertThat(first(suggested).getReputation(), is(NORMAL));

        assertThat(second(suggested).getName(), is("Light Salume"));
        assertThat(second(suggested).getFatPercentage(), is("31,00"));

        assertThat(third(suggested).getName(), is("Traditional Salume"));
        assertThat(third(suggested).getFatPercentage(), is("37,00"));
    }

    @Test
    public void expertProfile() throws ParseException {
        List<Product> suggested = underTest.productsForExpert();

        /*
         * Only two results here (the only two traditional products available),
         * since no ordinary products are going to be offered to experts (they would feel cheated).
         */

        assertThat(suggested, hasSize(2));

        assertThat(first(suggested).getId(), is(4L));
        assertThat(first(suggested).getName(), is("Traditional Salume"));
        assertThat(first(suggested).getPrice(), is(theAmountOf("EUR 41,60")));
        assertThat(first(suggested).getFatPercentage(), is("37,00"));
        assertThat(first(suggested).getReputation(), is(TRADITIONAL));

        assertThat(second(suggested).getName(), is("Premium Salume"));
        assertThat(second(suggested).getReputation(), is(TRADITIONAL));
    }

    @Test
    public void gourmetProfile() throws ParseException {
        List<Product> suggested = underTest.productsForGourmet();

        assertThat(suggested, hasSize(3));

        assertThat(first(suggested).getId(), is(5L));
        assertThat(first(suggested).getName(), is("Premium Salume"));
        assertThat(first(suggested).getPrice(), is(theAmountOf("EUR 73,23")));
        assertThat(first(suggested).getFatPercentage(), is("38,00"));
        assertThat(first(suggested).getReputation(), is(TRADITIONAL));

        assertThat(second(suggested).getName(), is("Not Light In Your Pocket"));
        assertThat(second(suggested).getPrice(), is(theAmountOf("EUR 57,37")));

        assertThat(third(suggested).getName(), is("Traditional Salume"));
        assertThat(third(suggested).getPrice(), is(theAmountOf("EUR 41,60")));
    }

    private <T> T first(List<T> list) {
        return list.get(0);
    }

    private <T> T second(List<T> list) {
        return list.get(1);
    }

    private <T> T third(List<T> list) {
        return list.get(2);
    }

    private MonetaryAmount theAmountOf(String value) throws ParseException {
        return moneyDealer.theAmountOf(value);
    }

}
