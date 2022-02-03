package com.zj.android.expensetracker.models;

import java.util.UUID;

public class Category {

    private final UUID mId;
    private String mName;

    public Category(String name) {
        mId = UUID.randomUUID();
        mName = name;
    }

    public UUID getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
