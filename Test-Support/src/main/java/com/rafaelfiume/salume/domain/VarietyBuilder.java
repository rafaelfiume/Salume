package com.rafaelfiume.salume.domain;

import lombok.ToString;

@ToString(includeFieldNames = true)
public class VarietyBuilder {

    private Long id;
    private String name;
    private String imageLink;

    public static VarietyBuilder variety(String name)  { return new VarietyBuilder().withDefaults().withName(name); }
    public static VarietyBuilder ofSalumi(String name) { return variety(name); }

    public VarietyBuilder withDefaults() {
        withId(1L);
        withName("Chorizo");
        withImageLink("3/3f/Palacioschorizo.jpg");
        return this;
    }

    public VarietyBuilder withId(long id) {
        this.id = id;
        return this;
    }

    public VarietyBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public VarietyBuilder withImageLink(String imageLink) {
        this.imageLink = imageLink;
        return this;
    }

    public VarietyBuilder withNoImageLink() {
        this.imageLink = "";
        return this;
    }

    public Variety build() {
        return new Variety(id, name, imageLink);
    }

}
