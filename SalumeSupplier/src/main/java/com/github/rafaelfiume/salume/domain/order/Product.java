package com.github.rafaelfiume.salume.domain.order;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class Product {

    private String name;

    private String description;

    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

        final Product other = (Product) obj;
        return new EqualsBuilder().append(name, other.name).append(description, other.description)
                .append(price, price).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(3, 7).append(name).append(description).append(price).hashCode();
    }

}
