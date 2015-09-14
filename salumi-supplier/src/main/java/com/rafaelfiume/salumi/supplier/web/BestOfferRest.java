package com.rafaelfiume.salumi.supplier.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BestOfferRest implements BestOfferWs {

    @RequestMapping(method = RequestMethod.GET, value= "best/{offer}/for/{customerProfile}")
    @ResponseBody
    public OfferingAdapter bestOffer(@PathVariable(value = "offer") String productCategory, String customerProfile) {
        
        OfferingAdapter adapter = new OfferingAdapter();
        adapter.setName("Offer Based on Customer Profile");
        adapter.setPrice("45,55");
        adapter.setProductName("Gorgonzola");
        adapter.setResult("Ok");
        
        return adapter;
    }

}
