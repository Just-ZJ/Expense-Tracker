package com.zj.android.expensetracker.models;

import com.zj.android.expensetracker.CustomDate;

import java.util.UUID;

public class Expense {

    private final UUID mId;
    private CustomDate mDate;
    private String mCategory;
    private String mDetails;
    private Double mAmount;

    public Expense(UUID uuid) {
        mId = uuid;
    }

    public Expense() {
        this(UUID.randomUUID());
    }

    public Expense(CustomDate date, String category, String details, Double amount) {
        this(UUID.randomUUID());
        setDate(date);
        setCategory(category);
        setDetails(details);
        setAmount(amount);
    }

    public UUID getId() {
        return mId;
    }

    public CustomDate getDate() {
        return mDate;
    }

    public void setDate(CustomDate date) {
        mDate = date;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
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
