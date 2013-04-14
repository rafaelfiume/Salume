package com.rafaelfiume.salumi.supplier.web;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.jayway.restassured.response.Response;

public class BestOfferingRestIT {

	private static final String BASE_PATH = "ble/supplier/rest";
	
	private static final String BEST_OFFERING_SERVICE = BASE_PATH + "/best/{offer}/for/{customer}";
	
	@Test
	public void shouldPickBestOfferingToPremiumCustomer() {
		// given
		final String offer = "salumi";
		final String customer = "premiun";
		
		// when
		final Response bestOfferingResponse = 
				expect().statusCode(200).when().get(BEST_OFFERING_SERVICE, offer, customer);
		
		// then
        final OfferingAdapter offering = 
                bestOfferingResponse.getBody().jsonPath().getObject("", OfferingAdapter.class);
        assertThat(offering.getResult(), equalTo("Ok"));
        assertThat(offering.getName(), equalTo("Offer Based on Customer Profile"));
        assertThat(offering.getProductName(), equalTo("Il Salume Piu' Caro"));
        assertThat(offering.getPrice(), equalTo("45.55"));
    }
	
	@Test
	public void shouldFailIfOfferRequestIsntValid() {
        final String offer = "batteries"; // Gioseppo doesn't sell batteries
        final String customer = "premiun";
        
        expect()
            .statusCode(200) // do we still continue to expect Http status code 200??
            .body("result", equalTo("Fail"))
            .body("error.description", equalTo("unknown product batteries"))
        .when().get(BEST_OFFERING_SERVICE, offer, customer);
	}
	
}
