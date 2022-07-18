package com.zj.android.expensetracker.database;

public class ExpenseDbSchema {
    public static final class ExpenseTable {
        public static final String NAME = "expenses";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String CATEGORY_UUID = "category_uuid";
            public static final String DETAILS = "details";
            public static final String AMOUNT = "amount";
        }
    }

    public static final class CategoryTable {
        public static final String NAME = "categories";

        // all unique categories of expenses
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
        }
    }
}