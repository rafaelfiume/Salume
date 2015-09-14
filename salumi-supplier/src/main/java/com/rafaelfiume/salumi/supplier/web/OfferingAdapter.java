package com.rafaelfiume.salumi.supplier.web;

public class OfferingAdapter {
    
    private RequestError error;

    private String result;

    private String name;

    private String price;

    private String productName;

    public RequestError getError() {
        return error;
    }
    
    public void setError(RequestError error) {
        this.error = error;
    }
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
