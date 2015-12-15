package com.rafaelfiume.salume;

import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.domain.Reputation;

public class ProductBuilder {

    private final String name;
    private String price;
    private String reputation;
    private String fat;

    public ProductBuilder(String name) {
        this.name = name;
    }

    public static ProductBuilder a(String name) {
        return new ProductBuilder(name);
    }

    public ProductBuilder at(String price) {
        this.price = price;
        return this;
    }

    public ProductBuilder regardedAs(String reputation) {
        this.reputation = reputation;
        return this;
    }

    public ProductBuilder with(String fat) {
        this.fat = fat;
        return this;
    }

    public ProductBuilder percentageOfFat() {
        // Just make the test read nicer. Use it after #with
        return this;
    }

    public Product build(MoneyDealer moneyDealer) {
        return new Product(
                1L, // Being ignored in the integration and acceptance tests
                this.name,
                moneyDealer.theAmountOf(this.price),
                this.fat,
                Reputation.valueOf(this.reputation));
    }
}
