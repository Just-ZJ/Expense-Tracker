package com.zj.android.expensetracker;

import java.util.List;
import java.util.UUID;

public class Expense {

    private UUID mId;
    private String mDate;
    private String mCategories;
    private String mDetails;
    private Double mAmount;

    public Expense(String date, String categories, String details, Double amount){
        mId = UUID.randomUUID();
        mDate = date;
        mDetails = details;
        mCategories = categories;
        mAmount = amount;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getCategories() {
        return mCategories;
    }

    public void setCategories(String categories) {
        mCategories = categories;
    }

    public String getDetails() {
        return mDetails;
    }

    public void setDetails(String details) {
        mDetails = details;
    }

    public Double getAmount() {
        return mAmount;
    }

    public void setAmount(Double amount) {
        mAmount = amount;
    }
}
