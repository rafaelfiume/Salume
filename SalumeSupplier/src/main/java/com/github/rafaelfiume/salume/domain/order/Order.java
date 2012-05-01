package com.github.rafaelfiume.salume.domain.order;

public class Order {

    private final long id = 1;

    private String product;

    public long getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }
    
    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", product=" + product + "]";
    }

}
