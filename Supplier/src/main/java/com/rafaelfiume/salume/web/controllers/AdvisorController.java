package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.services.ProductAdvisor;
import com.rafaelfiume.salume.web.result.MobileProductAdvisorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    private final ProductAdvisor productAdvisor;

    @Autowired
    public AdvisorController(ProductAdvisor productAdvisor) {
        this.productAdvisor = productAdvisor;
    }

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public MobileProductAdvisorResponse handle(@PathVariable String profile) {
        return MobileProductAdvisorResponse.of(productAdvisor.recommendationFor(profile));
    }

}
