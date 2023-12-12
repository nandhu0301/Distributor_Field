package com.example.distributor_field.bean;

public class OutstandingProducts {

    int id;
    String ProductName;
    String Qty;
    String Amount;

    public int getId() {
        return id;
    }

    public OutstandingProducts setId(int id) {
        this.id = id;
        return this;
    }

    public String getProductName() {
        return ProductName;
    }

    public OutstandingProducts setProductName(String productName) {
        ProductName = productName;
        return this;
    }

    public String getQty() {
        return Qty;
    }

    public OutstandingProducts setQty(String qty) {
        Qty = qty;
        return this;
    }

    public String getAmount() {
        return Amount;
    }

    public OutstandingProducts setAmount(String amount) {
        Amount = amount;
        return this;
    }
}
