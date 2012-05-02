package com.github.rafaelfiume.salume.web.rest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rafaelfiume.salume.domain.order.Item;
import com.github.rafaelfiume.salume.domain.order.Order;
import com.github.rafaelfiume.salume.domain.order.Product;

/** 
 * @author Rafael Fiume
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    @ResponseBody
    public Order findOrder(@PathVariable("id") long id) {
        Order order = new Order();
        order.setId(1);
        order.setItems(newOrderItems());
        return order;
    }
    
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Order> getAllOrders() {
        return newOrders();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Order add(@RequestBody Order order) {
        // TODO add order
        return order;
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    @ResponseBody
    public Order update(@PathVariable("id") long id, @RequestBody Order order) {
        return order;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    @ResponseBody
    public Order remove(@PathVariable("id") long id) {
        Order removed = new Order();
        removed.setId(1);
        removed.setItems(newOrderItems());
        return removed;
    }
    
    /*
     * Fake repository implementation.
     */
    
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
    
    private List<Order> newOrders() {
        Order first = new Order();
        first.setId(1);
        first.setItems(newOrderItems());
        
        Order second = new Order();
        second.setId(2);
        second.setItems(newOrderItems());
        
        List<Order> orders = new ArrayList<Order>();
        orders.add(first);
        orders.add(second);
        return orders;
    }

}
