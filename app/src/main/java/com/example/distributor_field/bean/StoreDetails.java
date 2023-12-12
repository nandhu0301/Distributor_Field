package com.example.distributor_field.bean;

import com.google.gson.JsonArray;

import org.json.JSONArray;

import java.util.ArrayList;

public class StoreDetails {

    int storeId;

    int id;
    String store_name;
    String store_owner_name;
    String store_landmark;
    String store_address;
    String store_gst_no;
    String phone_no;
    String alt_phone_no;
    String store_image;

    JSONArray taskcollection;

    String longitude;
    String latitude;
    String location_details;
    int checkin;


    public int getCheckin() {
        return checkin;
    }

    public void setCheckin(int checkin) {
        this.checkin = checkin;
    }

    public String getLongitude() {
        return longitude;
    }

    public StoreDetails setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public StoreDetails setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLocation_details() {
        return location_details;
    }

    public StoreDetails setLocation_details(String location_details) {
        this.location_details = location_details;
        return this;
    }

    ArrayList<taskcollection> taskcollectionArrayList = new ArrayList<>();

    public ArrayList<com.example.distributor_field.bean.taskcollection> getTaskcollectionArrayList() {
        return taskcollectionArrayList;
    }

    public StoreDetails setTaskcollectionArrayList(ArrayList<com.example.distributor_field.bean.taskcollection> taskcollectionArrayList) {
        this.taskcollectionArrayList = taskcollectionArrayList;
        return this;
    }

    public JSONArray getTaskcollection() {
        return taskcollection;
    }

    public StoreDetails setTaskcollection(JSONArray taskcollection) {
        this.taskcollection = taskcollection;
        return this;
    }

    public int getId() {
        return id;
    }

    public StoreDetails setId(int id) {
        this.id = id;
        return this;
    }

    public int getStoreId() {
        return storeId;
    }

    public StoreDetails setStoreId(int storeId) {
        this.storeId = storeId;
        return this;
    }

    public String getStore_name() {
        return store_name;
    }

    public StoreDetails setStore_name(String store_name) {
        this.store_name = store_name;
        return this;
    }

    public String getStore_owner_name() {
        return store_owner_name;
    }

    public StoreDetails setStore_owner_name(String store_owner_name) {
        this.store_owner_name = store_owner_name;
        return this;
    }

    public String getStore_landmark() {
        return store_landmark;
    }

    public StoreDetails setStore_landmark(String store_landmark) {
        this.store_landmark = store_landmark;
        return this;
    }

    public String getStore_address() {
        return store_address;
    }

    public StoreDetails setStore_address(String store_address) {
        this.store_address = store_address;
        return this;
    }

    public String getStore_gst_no() {
        return store_gst_no;
    }

    public StoreDetails setStore_gst_no(String store_gst_no) {
        this.store_gst_no = store_gst_no;
        return this;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public StoreDetails setPhone_no(String phone_no) {
        this.phone_no = phone_no;
        return this;
    }

    public String getAlt_phone_no() {
        return alt_phone_no;
    }

    public StoreDetails setAlt_phone_no(String alt_phone_no) {
        this.alt_phone_no = alt_phone_no;
        return this;
    }

    public String getStore_image() {
        return store_image;
    }

    public StoreDetails setStore_image(String store_image) {
        this.store_image = store_image;
        return this;
    }
}
