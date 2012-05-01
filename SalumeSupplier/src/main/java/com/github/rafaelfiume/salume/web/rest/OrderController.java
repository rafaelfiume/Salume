package com.github.rafaelfiume.salume.web.rest;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rafaelfiume.salume.domain.order.Order;
import com.github.rafaelfiume.salume.domain.order.Product;

/** 
 * @author Rafael Fiume
 */
@Controller
public class OrderController {

    
    @RequestMapping(method = RequestMethod.GET, value = "/order/{id}")
    @ResponseBody
    public Order findOrder(@PathVariable("id") long id) {
        Order order = new Order();
        order.setProduct(newMortadela());
        return order;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order")
    @ResponseBody
    public Order add(@RequestBody Order order) {
        // TODO add order
        return order;
    }
    
    @RequestMapping(method = RequestMethod.PUT, value = "/order/{id}")
    @ResponseBody
    public Order update(@PathVariable("id") long id, @RequestBody Order order) {
        return order;
    }
    
    @RequestMapping(method = RequestMethod.DELETE, value = "/order/{id}")
    @ResponseBody
    public Order remove(@PathVariable("id") long id) {
        Order removed = new Order();
        removed.setProduct(newMortadela());
        return removed;
    }
    
    private Product newMortadela() {
        Product mortadela = new Product();
        mortadela.setName("mortadela");
        mortadela.setDescription("carne defumada");
        mortadela.setPrice(new BigDecimal("23.4"));
        return mortadela;
    }

}
