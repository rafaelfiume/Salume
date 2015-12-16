package com.rafaelfiume.salume.domain.matchers;

import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.ProductBuilder;
import org.hamcrest.TypeSafeMatcher;

public abstract class AbstractAdvisedProductMatcherBuilder<Domain> {

    private final ProductBuilder expectedProduct;
    private final MoneyDealer moneyDealer;

    public AbstractAdvisedProductMatcherBuilder(MoneyDealer moneyDealer, String expectedProductName) {
        this.moneyDealer = moneyDealer;
        this.expectedProduct = new ProductBuilder(expectedProductName);
    }

    public AbstractAdvisedProductMatcherBuilder<Domain> at(String price) {
        this.expectedProduct.at(price);
        return this;
    }

    public AbstractAdvisedProductMatcherBuilder<Domain> regardedAs(String reputation) {
        this.expectedProduct.regardedAs(reputation);
        return this;
    }

    public AbstractAdvisedProductMatcherBuilder<Domain> with(String fat) {
        this.expectedProduct.with(fat);
        return this;
    }

    /*
     * Effectively builds the matcher making the test read nicer. Use it after #with.
     */
    public abstract TypeSafeMatcher<Domain> percentageOfFat();

    protected Product expectedProduct() {
        return expectedProduct.build(moneyDealer);
    }

    protected MoneyDealer moneyDealer() {
        return moneyDealer;
    }

}
