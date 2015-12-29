package com.rafaelfiume.salume.web.result;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;
import lombok.Getter;

@JacksonXmlRootElement(localName = "product")
public class ProductResponse {

    @Getter private final long id;
    @Getter private final String name;
    @Getter private final String price;
    private final String fatPercentage;
    @Getter private final String reputation;

    public ProductResponse(Product product, MoneyDealer moneyDealer) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = moneyDealer.format(product.getPrice());
        this.fatPercentage = product.getFatPercentage();
        this.reputation = ReputationRepresentation.of(product.getReputation());
    }

    @SuppressWarnings("unused")
    @JacksonXmlProperty(localName = "fat-percentage")
    public String getFatPercentage() {
        return fatPercentage;
    }

}
