package com.rafaelfiume.salume.web.result;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rafaelfiume.salume.domain.Product;

import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.util.Locale;

import static com.rafaelfiume.salume.domain.Product.Reputation.TRADITIONAL;
import static java.util.Locale.ITALY;

@JacksonXmlRootElement(localName = "product")
public class ProductResponse {

    private static final MonetaryAmountFormat moneyFormatter = MonetaryFormats.getAmountFormat(ITALY);

    private final long id;
    private final String name;
    private final String price;
    private final String fatPercentage;
    private final String reputation;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = moneyFormatter.format(product.getPrice());
        this.fatPercentage = product.getFatPercentage();
        // TODO RF 20/10/2015 Get special from properties
        this.reputation = product.getReputation() == TRADITIONAL ? "traditional" : "special";
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

    @JacksonXmlProperty(localName = "fat-percentage")
    public String getFatPercentage() {
        return fatPercentage;
    }

    public String getReputation() {
        return reputation;
    }
}
