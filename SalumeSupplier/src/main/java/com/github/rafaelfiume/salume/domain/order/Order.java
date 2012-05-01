package com.github.rafaelfiume.salume.domain.order;

import java.util.ArrayList;
import java.util.List;

public class Order {

    private long id;

    private List<Item> items = new ArrayList<Item>();
    
    public long getId() {
        return id;
    }
    
    public void setId(long id) {
        this.id = id;
    }

    public List<Item> getItems() {
        return items;
    }
    
    public void setItems(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + items + "]";
    }

}
