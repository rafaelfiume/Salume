package com.github.rafaelfiume.salume.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rafaelfiume.salume.domain.order.Order;

/**
 * TODO See about Spring and Rest Content Negotiation:
 * <ul>
 * </ul>
 * 
 * @author Rafael Fiume
 */
@Controller
public class OrderController {

    @RequestMapping(method = RequestMethod.GET, value = "/order/{id}")
    public @ResponseBody Order findOrder(@PathVariable("id") long id) {
        return new Order("mortadela");
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String add(@RequestBody Order order) {
        // TODO add order
        return "redirect:rest/order/" + order.getId();
    }

}
