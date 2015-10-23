package com.rafaelfiume.salume.domain;

import javax.money.MonetaryAmount;

public class Product {

    public enum Reputation { NORMAL, TRADITIONAL }

    private final Long id;
    private final String name;
    private final MonetaryAmount price;
    private final String fatPercentage;
    private final Reputation reputation;

    // TODO RF 20/10/2015
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
