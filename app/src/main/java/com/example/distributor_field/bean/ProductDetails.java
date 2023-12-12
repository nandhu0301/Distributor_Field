package com.example.distributor_field.bean;

public class ProductDetails {
    String productId;
    String productName;
    int productSellingPrice;
    int productQuantity;

    public String getProductId() {
        return productId;
    }

    public ProductDetails setProductId(String productId) {
        this.productId = productId;
        return this;
    }

    public String getProductName() {
        return productName;
    }

    public ProductDetails setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public int getProductSellingPrice() {
        return productSellingPrice;
    }

    public ProductDetails setProductSellingPrice(int productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
        return this;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public ProductDetails setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
        return this;
    }
}
