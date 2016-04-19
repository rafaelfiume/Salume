package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.services.ProductAdviser;
import com.rafaelfiume.salume.web.model.ProductAdviserMobileResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdviserController {

    private final ProductAdviser productAdviser;
    private final MoneyDealer moneyDealer;

    @Autowired
    public AdviserController(ProductAdviser productAdviser, MoneyDealer moneyDealer) {
        this.productAdviser = productAdviser;
        this.moneyDealer = moneyDealer;
    }

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public ProductAdviserMobileResponseModel handle(@PathVariable String profile) {
        return ProductAdviserMobileResponseModel.of(productAdviser.recommendationFor(profile), moneyDealer);
    }

}
