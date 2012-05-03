package com.github.rafaelfiume.salume.order.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final Order other = (Order) obj;
        return new EqualsBuilder().append(id, other.id).append(items, other.items)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(11, 13).append(id).append(items).hashCode();
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", items=" + items + "]";
    }

}
