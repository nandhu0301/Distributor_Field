package com.example.distributor_field.bean;

public class CustomData {
    private String product_id;
    private String  product_name;
    private int  product_quantity;
    private int  price;
    private int total_price;

    public CustomData(String product_id, String product_name, int product_quantity, int price, int total_price) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_quantity = product_quantity;
        this.price = price;
        this.total_price = total_price;
    }

    public String getProduct_id() {
        return product_id;
    }

    public CustomData setProduct_id(String product_id) {
        this.product_id = product_id;
        return this;
    }

    public String getProduct_name() {
        return product_name;
    }

    public CustomData setProduct_name(String product_name) {
        this.product_name = product_name;
        return this;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public CustomData setProduct_quantity(int product_quantity) {
        this.product_quantity = product_quantity;
        return this;
    }

    public int getPrice() {
        return price;
    }

    public CustomData setPrice(int price) {
        this.price = price;
        return this;
    }

    public int getTotal_price() {
        return total_price;
    }

    public CustomData setTotal_price(int total_price) {
        this.total_price = total_price;
        return this;
    }
}
