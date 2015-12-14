package com.rafaelfiume.salume.domain;

import javax.money.MonetaryAmount;

public class Product {

    private final Long id;
    private final String name;
    private final MonetaryAmount price;
    private final String fatPercentage;
    private final Reputation reputation;

    public Product(Long id, String name, MonetaryAmount price, String fatPercentage, Reputation reputation) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fatPercentage = fatPercentage;
        this.reputation = reputation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MonetaryAmount getPrice() {
        return price;
    }

    public String getFatPercentage() {
        return fatPercentage;
    }

    public Reputation getReputation() {
        return reputation;
    }
}
