package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.web.result.MobileProductAdvisorResponse;
import org.javamoney.moneta.Money;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;

import static com.rafaelfiume.salume.domain.Product.Reputation.NORMAL;
import static com.rafaelfiume.salume.domain.Product.Reputation.TRADITIONAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public MobileProductAdvisorResponse handle(@PathVariable String profile) {

        final Product cheap       = new Product(1L, "Cheap Salume", moneyOf(15.55), "49,99", NORMAL);
        final Product light       = new Product(2L, "Light Salume", moneyOf(29.55), "31,00", NORMAL);
        final Product traditional = new Product(3L, "Traditional Salume", moneyOf(41.60), "37,00", TRADITIONAL);

        final List<Product> products = new ArrayList() {{
            add(cheap);
            add(light);
            add(traditional);
        }};

        return MobileProductAdvisorResponse.of(products);
    }

    private MonetaryAmount moneyOf(Number money) {
        return Money.of(money, "EUR");
    }

}
