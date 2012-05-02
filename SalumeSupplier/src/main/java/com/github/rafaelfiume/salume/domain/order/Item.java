package com.github.rafaelfiume.salume.domain.order;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Item {

    private int quantity;

    private Product product;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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

        final Item other = (Item) obj;
        return new EqualsBuilder().append(quantity, other.quantity).append(product, other.product)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 7).append(quantity).append(product).hashCode();
    }

}
