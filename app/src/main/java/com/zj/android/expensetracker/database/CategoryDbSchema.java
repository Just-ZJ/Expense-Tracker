package com.zj.android.expensetracker.database;

public class CategoryDbSchema {
    public static final class CategoryTable {
        public static final String NAME = "categories";

        // all unique categories of expenses
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
        }
    }
}