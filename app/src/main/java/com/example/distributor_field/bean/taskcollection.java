package com.example.distributor_field.bean;

import java.security.SecureRandom;

public class taskcollection {

    String id;
    String store_name;
    String store_landmark;
    String date;

    public String getId() {
        return id;
    }

    public taskcollection setId(String id) {
        this.id = id;
        return this;
    }

    public String getStore_name() {
        return store_name;
    }

    public taskcollection setStore_name(String store_name) {
        this.store_name = store_name;
        return this;
    }

    public String getStore_landmark() {
        return store_landmark;
    }

    public taskcollection setStore_landmark(String store_landmark) {
        this.store_landmark = store_landmark;
        return this;
    }

    public String getDate() {
        return date;
    }

    public taskcollection setDate(String date) {
        this.date = date;
        return this;
    }
}
