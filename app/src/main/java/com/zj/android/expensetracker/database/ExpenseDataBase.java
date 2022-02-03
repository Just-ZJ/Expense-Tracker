package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable.Cols;
import com.zj.android.expensetracker.models.Expense;

public class ExpenseDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expense.db";

    public ExpenseDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, expense.getId().toString());
        values.put(Cols.DATE, expense.getDate());
        values.put(Cols.DETAILS, expense.getDetails());
        values.put(Cols.AMOUNT, expense.getAmount());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ExpenseTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.UUID + ", " +
                Cols.DATE + ", " +
                Cols.DETAILS + ", " +
                Cols.AMOUNT +
                ")"
        );
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseTable.NAME, null, getContentValues(expense));
        database.close();
    }

    public void updateExpense() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    public void removeExpense() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
        onCreate(sqLiteDatabase);
    }
}