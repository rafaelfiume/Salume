package com.rafaelfiume.salume.domain;

public class Product {

    public enum Reputation { NOT_TRADITIONAL, TRADITIONAL }

    private final long id;
    private final String name;
    private final String price;
    private final String fatPercentage;
    private final Reputation reputation;

    // TODO RF 20/10/2015
    public Product(long id, String name, String price, String fatPercentage, Reputation reputation) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fatPercentage = fatPercentage;
        this.reputation = reputation;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getFatPercentage() {
        return fatPercentage;
    }

    public Reputation getReputation() {
        return reputation;
    }
}
