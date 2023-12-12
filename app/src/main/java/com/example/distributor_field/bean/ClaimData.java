package com.example.distributor_field.bean;

public class ClaimData {
    private int ClaimId;
    private String CreateDate;
    private String AmountStatus;
    private String OutstandingAmount;

    public int getClaimId() {
        return ClaimId;
    }

    public ClaimData setClaimId(int claimId) {
        ClaimId = claimId;
        return this;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public ClaimData setCreateDate(String createDate) {
        CreateDate = createDate;
        return this;
    }

    public String getAmountStatus() {
        return AmountStatus;
    }

    public ClaimData setAmountStatus(String amountStatus) {
        AmountStatus = amountStatus;
        return this;
    }

    public String getOutstandingAmount() {
        return OutstandingAmount;
    }

    public ClaimData setOutstandingAmount(String outstandingAmount) {
        OutstandingAmount = outstandingAmount;
        return this;
    }
}
