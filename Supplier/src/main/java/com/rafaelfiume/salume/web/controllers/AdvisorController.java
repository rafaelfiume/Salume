package com.rafaelfiume.salume.web.controllers;

import com.rafaelfiume.salume.domain.Product;
import com.rafaelfiume.salume.web.views.MobileAppProductView;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.rafaelfiume.salume.domain.Product.Reputation.NOT_TRADITIONAL;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/xml")
    public MobileAppProductView handle(@PathVariable String profile) {

        return new MobileAppProductView(new Product(1L, "Cheap Salume", "15.55", "49.99", NOT_TRADITIONAL));
    }

}
