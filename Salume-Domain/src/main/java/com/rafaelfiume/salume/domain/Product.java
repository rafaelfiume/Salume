package com.rafaelfiume.salume.domain;

import lombok.Getter;
import lombok.ToString;

import javax.money.MonetaryAmount;

import static com.rafaelfiume.salume.domain.Product.UrlLinkBuilder.descriptionUrlFor;
import static com.rafaelfiume.salume.domain.Product.UrlLinkBuilder.imageUrlFor;

@ToString(includeFieldNames = true)
@SuppressWarnings("unused")
public class Product {

    @Getter private final Long id;
    @Getter private final String name;
    @Getter private final MonetaryAmount price;
    @Getter private final String fatPercentage;
    @Getter private final Reputation reputation;
    @Getter private final Variety variety; /* Avoid using it directly */

    public Product(Long id, String name, MonetaryAmount price, String fatPercentage, Reputation reputation, Variety variety) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.fatPercentage = fatPercentage;
        this.reputation = reputation;
        this.variety = variety;
    }

    public String getVarietyName() {
        return variety.getName();
    }

    public String getImageUrl() {
        return imageUrlFor(variety.getImageLink());
    }

    public String getDescriptionUrl() {
        return descriptionUrlFor(getVarietyName());
    }

    static class UrlLinkBuilder {

        private UrlLinkBuilder() {
            // Not intended to be instantiated
        }

        static String imageUrlFor(String image) {
            return "https://upload.wikimedia.org/wikipedia/commons/" + image;
        }

        static String descriptionUrlFor(String variety) {
            return "https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles=" + variety;
        }
    }
}
