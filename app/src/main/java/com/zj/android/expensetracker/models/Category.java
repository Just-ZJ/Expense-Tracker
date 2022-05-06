package com.zj.android.expensetracker.models;

import java.util.UUID;

public class Category {

    private final UUID mId;
    private String mName;

    public Category(UUID uuid) {
        mId = uuid;
    }

    public Category() {
        this(UUID.randomUUID());
    }

    public Category(String name) {
        this(UUID.randomUUID());
        setName(name);
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
