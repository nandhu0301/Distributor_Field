package com.example.distributor_field.bean;

import java.util.ArrayList;

public class Outstanding {

    int StoreOutStanding;
    int BillCount;
    ArrayList<dataCollection> dataCollections = new ArrayList<>();
    ArrayList<list> listArrayList = new ArrayList<>();

    public int getStoreOutStanding() {
        return StoreOutStanding;
    }

    public Outstanding setStoreOutStanding(int storeOutStanding) {
        StoreOutStanding = storeOutStanding;
        return this;
    }

    public int getBillCount() {
        return BillCount;
    }

    public Outstanding setBillCount(int billCount) {
        BillCount = billCount;
        return this;
    }

    public ArrayList<dataCollection> getDataCollections() {
        return dataCollections;
    }

    public Outstanding setDataCollections(ArrayList<dataCollection> dataCollections) {
        this.dataCollections = dataCollections;
        return this;
    }

    public ArrayList<list> getListArrayList() {
        return listArrayList;
    }

    public Outstanding setListArrayList(ArrayList<list> listArrayList) {
        this.listArrayList = listArrayList;
        return this;
    }
}
