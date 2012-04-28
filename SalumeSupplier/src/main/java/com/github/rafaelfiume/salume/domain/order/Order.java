package com.github.rafaelfiume.salume.domain.order;

public class Order {
    
    private final long id = 1;

    private final String order;

    public Order(String order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }
    
    public String getOrder() {
        return order;
    }
    
}
