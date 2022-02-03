package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.Expense;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;

public class ExpenseDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expenseBase.db";

    public ExpenseDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ExpenseTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ExpenseTable.Cols.DATE + ", " +
                ExpenseTable.Cols.CATEGORIES + ", " +
                ExpenseTable.Cols.DETAILS + ", " +
                ExpenseTable.Cols.AMOUNT +
                ")"
        );
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.Cols.DATE, expense.getDate());
        values.put(ExpenseTable.Cols.CATEGORIES, expense.getCategories());
        values.put(ExpenseTable.Cols.DETAILS, expense.getDetails());
        values.put(ExpenseTable.Cols.AMOUNT, expense.getAmount());
        return values;
    }

    public void addExpense(Expense expense){
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseTable.NAME, null, getContentValues(expense));
        database.close();
    }

    public void updateExpense(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    public void removeExpense(){
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
        onCreate(sqLiteDatabase);
    }
}