package com.github.rafaelfiume.salume.web.rest;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.rafaelfiume.salume.domain.order.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("OrderControllerTest-context.xml")
public class OrderControllerTest {

    @Autowired
    private OrderController oController;
 
    @Test
    public void testFindOrder() {
        Order expected = new Order();
        // TODO
        Assert.assertEquals(expected, oController.findOrder(1));
    }
    
}
