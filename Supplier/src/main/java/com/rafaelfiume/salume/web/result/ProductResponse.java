package com.rafaelfiume.salume.web.result;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;

@JacksonXmlRootElement(localName = "product")
public class ProductResponse {

    private final long id;
    private final String name;
    private final String price;
    private final String fatPercentage;
    private final String reputation;

    public ProductResponse(Product product, MoneyDealer moneyDealer) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = moneyDealer.format(product.getPrice());
        this.fatPercentage = product.getFatPercentage();
        this.reputation = ReputationRepresentation.of(product.getReputation());
    }

    @SuppressWarnings("unused")
    public long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getPrice() {
        return price;
    }

    @SuppressWarnings("unused")
    @JacksonXmlProperty(localName = "fat-percentage")
    public String getFatPercentage() {
        return fatPercentage;
    }

    @SuppressWarnings("unused")
    public String getReputation() {
        return reputation;
    }
}