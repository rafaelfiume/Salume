package com.rafaelfiume.salume.domain;

import lombok.Getter;
import lombok.ToString;

@ToString
public class Variety {

    @Getter private final Long id;
    @Getter private final String name;
    @Getter private final String imageLink;

    public Variety(Long id, String name, String imageLink) {
        this.id = id;
        this.name = name;
        this.imageLink = imageLink;
    }

}
