package com.github.rafaelfiume.salume.order.controller.rest;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.rafaelfiume.salume.order.controller.rest.OrderController;

/**
 * @author Rafael Fiume
 */
@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration defaults to OrderControllerTest-context.xml in the same package
@ContextConfiguration
public class OrderControllerTest {

    @Test
    public void testFindOrder() throws Exception {
        //@formatter:off
        standaloneSetup(new OrderController())
                .build()
                .perform(get("/order/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_JSON))
                .andExpect(content().string(findOrderJsonResult()))
                .andExpect(jsonPath("$.id").value(1)).andDo(print());
        //@formatter:on
    }

    ////
    ////
    ////// EXPECTATIONS
    ////
    ////
    
    private String findOrderJsonResult() throws IOException {
        return jsonResultFromFile("expectation/OrderControllerTest-findOrder-json.txt");
    }
    
    private String jsonResultFromFile(String filePath) throws IOException {
        final URL url = getClass().getResource(filePath);
        final List<String> lines = FileUtils.readLines(new File(url.getFile()));
        final StringBuilder builder = new StringBuilder();
        for (String line : lines) {
            builder.append(StringUtils.trim(line));
        }
        return builder.toString();
    }

}
