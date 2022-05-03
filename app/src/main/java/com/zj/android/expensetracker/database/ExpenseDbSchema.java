package com.zj.android.expensetracker.database;

public class ExpenseDbSchema {
    public static final class ExpenseTable {
        public static final String NAME = "expenses";

        public static final class Cols {
            public static final String UUID = "uuid";
//            public static final Date DATE = new Date();
public static final String DATE = "date";
            public static final String DETAILS = "details";
            public static final String AMOUNT = "amount";
        }
    }
}