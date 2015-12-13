package com.rafaelfiume.salume.services;

import com.rafaelfiume.salume.db.advisor.PersistentProductBase;
import com.rafaelfiume.salume.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@Transactional
public class ProductAdvisor {

    private final PersistentProductBase productAdvisorDao;

    @Autowired
    public ProductAdvisor(PersistentProductBase productAdvisorDao) {
        this.productAdvisorDao = productAdvisorDao;
    }

    @Transactional
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
