package com.rafaelfiume.salume.domain;


import org.junit.Test;

import static com.rafaelfiume.salume.domain.ProductTest.ProductBuilder.a;
import static com.rafaelfiume.salume.domain.ProductTest.VarietyBuilder.variety;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ProductTest {

    @Test
    public void shouldReturnImageAndDescriptionUrlsAccordingToVariety() {
        Product salame = a("Salume Gourmet D.O.P")
                .with(variety("Skilandis").withImageLink("2/25/Skilandis2.jpg"));

        assertThat(salame.getVarietyName()   , is(equalTo("Skilandis")));
        assertThat(salame.getImageUrl()      , is(equalTo("https://upload.wikimedia.org/wikipedia/commons/2/25/Skilandis2.jpg")));
        assertThat(salame.getDescriptionUrl(), is(equalTo("https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles=Skilandis")));
    }

    @Test
    public void shouldReturnDefaultImageUrlsWhenImageLinkIsNull() {
        Product salame = a("Salume Gourmet D.O.P")
                .with(variety("Skilandis").withImageLink(null));

        assertThat(salame.getVarietyName()   , is(equalTo("Skilandis")));
        assertThat(salame.getImageUrl()      , is(equalTo("https://upload.wikimedia.org/wikipedia/commons/b/b5/Formaggi_e_salumi_sardi.jpg")));
        assertThat(salame.getDescriptionUrl(), is(equalTo("https://it.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles=Skilandis")));
    }

    static class ProductBuilder {

        private final String name;

        public ProductBuilder(String name) {
            this.name = name;
        }

        public static ProductBuilder a(String name) {
            return new ProductBuilder(name);
        }

        public Product with(VarietyBuilder varietyBuilder) { // also builds product
            return new Product(-1L, this.name, null, null, null, varietyBuilder.build());
        }
    }

    static class VarietyBuilder {

        private String name;
        private String imageLink;

        public static VarietyBuilder variety(String name) { return new VarietyBuilder().withName(name); }

        public VarietyBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public VarietyBuilder withImageLink(String imageLink) {
            this.imageLink = imageLink;
            return this;
        }

        public Variety build() {
            return new Variety(-1L, name, imageLink);
        }

    }
}
