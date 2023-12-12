package com.example.distributor_field.bean;

import java.util.ArrayList;
import java.util.List;

public class dataCollection {

    int BillNo;
    String  PurchasedAmount;
    String OutstandingAmount;
    String BalanceAmount;


    private List<List<ClaimData>> list;
    public int getBillNo() {
        return BillNo;
    }

    public dataCollection setBillNo(int billNo) {
        BillNo = billNo;
        return this;
    }

    public String getPurchasedAmount() {
        return PurchasedAmount;
    }

    public dataCollection setPurchasedAmount(String purchasedAmount) {
        PurchasedAmount = purchasedAmount;
        return this;
    }

    public String getOutstandingAmount() {
        return OutstandingAmount;
    }

    public dataCollection setOutstandingAmount(String outstandingAmount) {
        OutstandingAmount = outstandingAmount;
        return this;
    }

    public String getBalanceAmount() {
        return BalanceAmount;
    }

    public dataCollection setBalanceAmount(String balanceAmount) {
        BalanceAmount = balanceAmount;
        return this;
        }
    public List<List<ClaimData>> getList() {
        return list;
    }

    public void setList(List<List<ClaimData>> list) {
        this.list = list;
    }
}
