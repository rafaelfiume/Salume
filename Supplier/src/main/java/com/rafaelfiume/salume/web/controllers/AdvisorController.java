package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.domain.MoneyDealer;
import com.rafaelfiume.salume.services.ProductAdvisor;
import com.rafaelfiume.salume.web.model.MobileProductAdviserResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    private final ProductAdvisor productAdvisor;
    private final MoneyDealer moneyDealer;

    @Autowired
    public AdvisorController(ProductAdvisor productAdvisor, MoneyDealer moneyDealer) {
        this.productAdvisor = productAdvisor;
        this.moneyDealer = moneyDealer;
    }

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public MobileProductAdviserResponseModel handle(@PathVariable String profile) {
        return MobileProductAdviserResponseModel.of(productAdvisor.recommendationFor(profile), moneyDealer);
    }

}
