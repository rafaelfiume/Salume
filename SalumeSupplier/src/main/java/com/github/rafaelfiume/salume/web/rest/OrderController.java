package com.github.rafaelfiume.salume.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rafaelfiume.salume.domain.order.Order;

/** 
 * @author Rafael Fiume
 */
@Controller
public class OrderController {

    @RequestMapping(method = RequestMethod.GET, value = "/order/{id}")
    @ResponseBody
    public Order findOrder(@PathVariable("id") long id) {
        Order order = new Order();
        order.setProduct("mortadela");
        return order;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/order")
    @ResponseBody
    public Order add(@RequestBody Order order) {
        // TODO add order
        return order;
    }

}
