package com.example.distributor_field.bean;

public class Category {
    String categoryId;
    String categoryName;

    public String getCategoryId() {
        return categoryId;
    }

    public Category setCategoryId(String categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public Category setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }
}
