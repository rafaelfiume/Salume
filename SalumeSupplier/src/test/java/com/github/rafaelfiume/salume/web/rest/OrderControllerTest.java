package com.github.rafaelfiume.salume.web.rest;

import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.server.setup.MockMvcBuilders.standaloneSetup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
// @ContextConfiguration defaults to OrderControllerTest-context.xml in the same package
@ContextConfiguration
public class OrderControllerTest {

    @Test
    public void testFindOrder() throws Exception {
        standaloneSetup(new OrderController())
                .build()
                .perform(get("/order/2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().type(MediaType.APPLICATION_JSON))
                .andExpect(
                        content()
                                .string("{\"id\":1,\"items\":[{\"quantity\":2,\"product\":{\"name\":\"Mortadela\",\"description\":\"Carne defumada\",\"price\":23.4}},{\"quantity\":5,\"product\":{\"name\":\"Salume Speciale\",\"description\":\"Gusto Incredibile\",\"price\":53.9}}]}"))
                .andExpect(jsonPath("$.id").value(1)).andDo(print());
    }


}
