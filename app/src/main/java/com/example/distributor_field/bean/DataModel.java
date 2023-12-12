package com.example.distributor_field.bean;

import java.util.List;

public class DataModel {
    private int storeOutStanding;
    private int billCount;
    private List<dataCollection> dataCollection;

    // Add getters and setters for the fields

    public int getStoreOutStanding() {
        return storeOutStanding;
    }

    public void setStoreOutStanding(int storeOutStanding) {
        this.storeOutStanding = storeOutStanding;
    }

    public int getBillCount() {
        return billCount;
    }

    public void setBillCount(int billCount) {
        this.billCount = billCount;
    }

    public List<dataCollection> getDataCollection() {
        return dataCollection;
    }

    public void setDataCollection(List<dataCollection> dataCollection) {
        this.dataCollection = dataCollection;
    }
}
