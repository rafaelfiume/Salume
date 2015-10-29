package com.rafaelfiume.salume.support;

public enum Applications {

    CLIENT("Client"), CUSTOMER("Customer"), SUPPLIER("Supplier");

    private String appName;

    Applications(String appName) {
        this.appName = appName;
    }

    public String appName() {
        return appName;
    }
}
