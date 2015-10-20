package com.rafaelfiume.salume.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.xml.MappingJackson2XmlView;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        final MappingJackson2XmlView xmlView = new MappingJackson2XmlView();
        xmlView.setPrettyPrint(true);

        registry.enableContentNegotiation(xmlView);
    }
}
