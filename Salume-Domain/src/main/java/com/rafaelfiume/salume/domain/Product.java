package com.rafaelfiume.salume.domain;

import lombok.Getter;
import lombok.ToString;

import javax.money.MonetaryAmount;

@ToString(includeFieldNames = true)
@SuppressWarnings("unused")
public class Product {

    @Getter private final Long id;
    @Getter private final String name;
    @Getter private final MonetaryAmount price;
    @Getter private final String fatPercentage;
    @Getter private final Reputation reputation;

    public Product(Long id, String name, MonetaryAmount price, String fatPercentage, Reputation reputation) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fatPercentage = fatPercentage;
        this.reputation = reputation;
    }

}
