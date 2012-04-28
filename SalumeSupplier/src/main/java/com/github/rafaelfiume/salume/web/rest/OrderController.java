package com.github.rafaelfiume.salume.web.rest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.rafaelfiume.salume.domain.order.Order;

/**
 * Some useful texts about Rest, Spring 3 and Json:
 * <ul>
 * <li>
 * <a
 * href="http://www.ibm.com/developerworks/webservices/library/wa-spring3webserv/index.html">Build
 * RESTful web services using Spring 3</a></li>
 * <li>
 * <a href="http://www.javacodegeeks.com/2010/06/spring-3-restful-web-services.html">Spring 3
 * RESTful Web Services</a></li>
 * <li>
 * <a href="http://java.dzone.com/articles/spring-3-rest-json-path-variables">Spring 3 and JSON</a></li>
 * <li>
 * <a href="http://www.mkyong.com/spring-mvc/spring-3-mvc-and-json-example/">Spring 3 MVC and JSON
 * example</a></li>
 * </ul>
 * 
 * @author Rafael Fiume
 */
@Controller
public class OrderController {

    /*
     * "Here, the @RequestBody annotation instructs Spring MVC to map the body of the HTTP request
     * to an Account object. Spring MVC knows to map from JSON because the client set the request
     * Content Type to application/json." See
     * http://blog.springsource.com/2010/01/25/ajax-simplifications-in-spring-3-0/
     */
    @RequestMapping(method = RequestMethod.GET, value = "order/{id}")
    public @ResponseBody Order findOrder(@PathVariable("id") long id) {
        return new Order("mortadela");
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String add(@RequestBody Order order) {
        // TODO add order
        return "redirect:rest/order/" + order.getId();
    }

}
