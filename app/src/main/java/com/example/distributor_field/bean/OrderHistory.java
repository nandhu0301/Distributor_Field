package com.example.distributor_field.bean;

public class OrderHistory {

    int BillNo;
    String TotalPrice;
    String CreatedDate;
    String storeName;
    String InvoiceUrl;

    public String getStoreName() {
        return storeName;
    }

    public OrderHistory setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public int getBillNo() {
        return BillNo;
    }

    public OrderHistory setBillNo(int billNo) {
        BillNo = billNo;
        return this;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public OrderHistory setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
        return this;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public OrderHistory setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
        return this;
    }

    public String getInvoiceUrl() {
        return InvoiceUrl;
    }

    public OrderHistory setInvoiceUrl(String invoiceUrl) {
        InvoiceUrl = invoiceUrl;
        return this;
    }
}
