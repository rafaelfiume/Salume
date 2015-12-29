package com.rafaelfiume.salume.web.result;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.domain.Product;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "product-advisor")
public class MobileProductAdvisorResponse {

    @JacksonXmlElementWrapper(useWrapping = false)
    private final List<ProductResponse> products = new ArrayList<>();

    public static MobileProductAdvisorResponse of(List<Product> products, MoneyDealer moneyDealer) {
        final MobileProductAdvisorResponse advisorView = new MobileProductAdvisorResponse();
        for (Product p : products) {
            advisorView.add(new ProductResponse(p, moneyDealer));
        }
        return advisorView;
    }

    private MobileProductAdvisorResponse() {
        // Use the method factory #of instead
    }

    @JacksonXmlElementWrapper(localName = "products")
    @JacksonXmlProperty(localName = "product")
    @SuppressWarnings("unused")
    public List<ProductResponse> getProducts() {
        return products;
    }

    private void add(ProductResponse pv) {
        products.add(pv);
    }

}
