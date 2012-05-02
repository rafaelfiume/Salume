package com.github.rafaelfiume.salume.web.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.github.rafaelfiume.salume.domain.order.Item;
import com.github.rafaelfiume.salume.domain.order.Order;
import com.github.rafaelfiume.salume.domain.order.Product;
import com.github.rafaelfiume.salume.util.test.IntegrationTest;

@Category(IntegrationTest.class)
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration defaults to OrderControllerTest-context.xml in the same package
@ContextConfiguration("OrderControllerTest-context.xml")
public class OrderControllerIntegrationTest {

    private static final String REST_SERVICE = "http://localhost:8080/Salume/restService/order";

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testFindOrder() {
        // TODO This duplication will be removed in a further step
        Order expected = new Order();
        expected.setId(1);
        expected.setItems(newOrderItems());

        Order result = restTemplate.getForObject(REST_SERVICE + "/{id}", Order.class, 2);
        Assert.assertEquals(expected, result);
    }

    // TODO This duplication will be removed in a further step
    private List<Item> newOrderItems() {
        Product mortadela = new Product();
        mortadela.setName("Mortadela");
        mortadela.setDescription("Carne defumada");
        mortadela.setPrice(new BigDecimal("23.4"));

        Product salumeSpeciale = new Product();
        salumeSpeciale.setName("Salume Speciale");
        salumeSpeciale.setDescription("Gusto Incredibile");
        salumeSpeciale.setPrice(new BigDecimal("53.9"));

        Item stItem = new Item();
        stItem.setQuantity(2);
        stItem.setProduct(mortadela);

        Item ndItem = new Item();
        ndItem.setQuantity(5);
        ndItem.setProduct(salumeSpeciale);

        final List<Item> items = new ArrayList<Item>();
        items.add(stItem);
        items.add(ndItem);

        return items;
    }

}
