package com.example.distributor_field.bean;

public class CategoryDetails {
    int id;
    String category_name;
    String category_image;

    public int getId() {
        return id;
    }

    public CategoryDetails setId(int id) {
        this.id = id;
        return this;
    }

    public String getCategory_name() {
        return category_name;
    }

    public CategoryDetails setCategory_name(String category_name) {
        this.category_name = category_name;
        return this;
    }

    public String getCategory_image() {
        return category_image;
    }

    public CategoryDetails setCategory_image(String category_image) {
        this.category_image = category_image;
        return this;
    }
}
