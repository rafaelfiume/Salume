package com.rafaelfiume.salume.services;

import com.rafaelfiume.salume.db.advisor.JdbcProductAdvisorDao;
import com.rafaelfiume.salume.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.String.format;

@Component
public class ProductAdvisor {

    private final JdbcProductAdvisorDao productAdvisorDao;

    @Autowired
    public ProductAdvisor(JdbcProductAdvisorDao productAdvisorDao) {
        this.productAdvisorDao = productAdvisorDao;
    }

    public List<Product> recommendationFor(String profile) {
        // TODO RF 24/10/2015 First-cut implementation. Improve this non O.O switch impl.

        switch (profile) {
            case "Magic": return productAdvisorDao.productsForMagic();
            case "Healthy": return productAdvisorDao.productsForHealthy();
            case "Expert": return productAdvisorDao.productsForExpert();
            case "Gourmet": return productAdvisorDao.productsForGourmet();

            default: throw new IllegalArgumentException(format("invalid profile %s", profile)); // // TODO RF 24/10/2015 BLERRGH! Sad path coming soon... eventually
        }
    }

}
