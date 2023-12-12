package com.example.distributor_field.bean;

public class list {

    int ClaimId;
    String CreateDate;
    String AmountStatus;
    String OutstandingAmount;

    public int getClaimId() {
        return ClaimId;
    }

    public list setClaimId(int claimId) {
        ClaimId = claimId;
        return this;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public list setCreateDate(String createDate) {
        CreateDate = createDate;
        return this;
    }

    public String getAmountStatus() {
        return AmountStatus;
    }

    public list setAmountStatus(String amountStatus) {
        AmountStatus = amountStatus;
        return this;
    }

    public String getOutstandingAmount() {
        return OutstandingAmount;
    }

    public list setOutstandingAmount(String outstandingAmount) {
        OutstandingAmount = outstandingAmount;
        return this;
    }
}
