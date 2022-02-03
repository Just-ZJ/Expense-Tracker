package com.zj.android.expensetracker.database;

public class ExpenseDbSchema {
    public static final class ExpenseTable {
        public static final String NAME = "expenses";

        public static final class Cols {
            public static final String DATE = "uuid";
            public static final String CATEGORIES = "title";
            public static final String DETAILS = "solved";
            public static final String AMOUNT = "date";
        }
    }
}