package com.zj.android.expensetracker.database;

public class ExpenseToCategoryDbSchema {
    public static final class ExpenseToCategoryTable {
        public static final String NAME = "expenseToCategories";

        // all unique categories of expenses
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String EXPENSE_UUID = "expense_uuid";
            public static final String CATEGORY_UUID = "category_uuid";
        }
    }
}