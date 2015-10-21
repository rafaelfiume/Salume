package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.web.result.MobileProductAdvisorResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.rafaelfiume.salume.domain.Product.Reputation.NOT_TRADITIONAL;
import static com.rafaelfiume.salume.domain.Product.Reputation.TRADITIONAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public MobileProductAdvisorResponse handle(@PathVariable String profile) {

        final Product cheap = new Product(1L, "Cheap Salume", "15.55", "49.99", NOT_TRADITIONAL);
        final Product light = new Product(2L, "Light Salume", "29.55", "31.00", NOT_TRADITIONAL);
        final Product traditional = new Product(3L, "Traditional Salume", "41.60", "37.00", TRADITIONAL);

        final List<Product> products = new ArrayList() {{
            add(cheap);
            add(light);
            add(traditional);
        }};

        return MobileProductAdvisorResponse.of(products);
    }

}
