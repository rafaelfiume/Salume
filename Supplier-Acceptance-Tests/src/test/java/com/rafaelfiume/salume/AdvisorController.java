package com.rafaelfiume.salume;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AdvisorController {

    @RequestMapping(value = "/advise/for/{profile}", method = GET, produces = "application/json")
    public String handle(@PathVariable String profile) {

        return "cheapest";
    }

}
