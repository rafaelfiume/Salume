package com.github.rafaelfiume.salume.domain.order;

public class Order {

    private final long id = 1;

    private String order;

    public Order(String order) {
        this.order = order;
    }

    public long getId() {
        return id;
    }

    public String getOrder() {
        return order;
    }
    
    public void setOrder(String order) {
        this.order = order;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + ((order == null) ? 0 : order.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Order other = (Order) obj;
        if (id != other.id)
            return false;
        if (order == null) {
            if (other.order != null)
                return false;
        } else if (!order.equals(other.order))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Order [id=" + id + ", order=" + order + "]";
    }

}
