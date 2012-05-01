package com.github.rafaelfiume.salume.domain.order;

public class Order {

    private final long id = 1;

    private Product product;

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", product=" + product + "]";
    }

}
