package com.zj.android.expensetracker.models;

import java.util.UUID;

public class ExpenseToCategory {

    private final UUID mId;
    private UUID mExpenseId;
    private UUID mCategoryId;

    public ExpenseToCategory(UUID uuid) {
        mId = uuid;
    }

    public ExpenseToCategory() {
        this(UUID.randomUUID());
    }

    public UUID getId() {
        return mId;
    }

    public UUID getExpenseId() {
        return mExpenseId;
    }

    public void setExpenseId(UUID expenseId) {
        mExpenseId = expenseId;
    }

    public UUID getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(UUID categoryId) {
        mCategoryId = categoryId;
    }
}
