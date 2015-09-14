package com.rafaelfiume.salumi.supplier.domain;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class BestOfferAlgorithmTest {

    @Ignore
	@Test
	public void shouldOfferToMagicTheCheapestProduct() {
		// given
		final List<Product> salumes = listOfSalumes();

		// when
		final Product result = new BestOffer(salumes).to(null); // CustomerProfile.MAGIC

		// then
		assertEquals(cheapestInTheWorld(), result);
	}

    @Ignore
	@Test
	public void shouldOfferToHealthyTheProductWithLessFat() {
		// given
		final List<Product> salumes = listOfSalumes();

		// when
		final Product result = new BestOffer(salumes).to(null); // CustomerProfile.HEALTHY

		// then
		assertEquals(lessFat(), result);
	}

    @Ignore
	@Test
	public void shouldOfferToPremiumTheMostExpensiveProduct() {
		// given
		final List<Product> salumes = listOfSalumes();

		// when
		final Product result = new BestOffer(salumes).to(null); // CustomerProfile.PREMIUM

		// then
		assertEquals(reallyExpensiceGourmet(), result);
	}

    @Ignore
	@Test
	public void shouldOfferToGourmetsTheBestProductsTheyCanAfford() {
		// given
		final List<Product> salumes = listOfSalumes();

		// when
		final Product result = new BestOffer(salumes).to(null); // CustomerProfile.GOURMET

		// then
		assertEquals(cheapestInTheWorld(), result);
	}

	private Product lessFat() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private Product cheap() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private Product cheapestInTheWorld() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private Product expensiveGourmet() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private Product reallyExpensiceGourmet() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private Product notThatGourmetButAffordable() {
		throw new UnsupportedOperationException("not yet implemented");
	}
	
	private Product expensiveTraditional() {
		throw new UnsupportedOperationException("not yet implemented");
	}

	private List<Product> listOfSalumes() {
		final List<Product> salumes = new ArrayList<Product>();
		
		salumes.add(lessFat());
		salumes.add(cheap());
		salumes.add(cheapestInTheWorld());
		salumes.add(expensiveGourmet());
		salumes.add(reallyExpensiceGourmet());
		salumes.add(notThatGourmetButAffordable());
		salumes.add(expensiveTraditional());
		
		return salumes;
	}

}
