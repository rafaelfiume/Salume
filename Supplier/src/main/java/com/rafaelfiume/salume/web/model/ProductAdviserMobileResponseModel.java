package com.rafaelfiume.salume.web.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "product-advisor")
public class ProductAdviserMobileResponseModel {

    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<ProductResponseModel> products = new ArrayList<>();

    public static ProductAdviserMobileResponseModel of(List<Product> products, MoneyDealer moneyDealer) {
        final ProductAdviserMobileResponseModel advisorView = new ProductAdviserMobileResponseModel();
        for (Product p : products) {
            advisorView.add(new ProductResponseModel(p, moneyDealer));
        }
        return advisorView;
    }

    private ProductAdviserMobileResponseModel() {
        // Use the method factory #of instead
    }

    @JacksonXmlElementWrapper(localName = "products")
    @JacksonXmlProperty(localName = "product")
    @SuppressWarnings("unused")
    public List<ProductResponseModel> getProducts() {
        return products;
    }

    private void add(ProductResponseModel pv) {
        products.add(pv);
    }

}
