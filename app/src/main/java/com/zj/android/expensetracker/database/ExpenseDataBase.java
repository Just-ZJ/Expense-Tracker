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
        // date is stored as long using int type in database
        values.put(Cols.DATE, expense.getDate().getTime());
        values.put(Cols.DETAILS, expense.getDetails());
        values.put(Cols.AMOUNT, expense.getAmount());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + ExpenseTable.NAME + "(" +
                " id integer PRIMARY KEY autoincrement, " +
                Cols.UUID + " text NOT NULL UNIQUE, " +
                Cols.DATE + " int NOT NULL, " +
                Cols.DETAILS + " text, " +
                Cols.AMOUNT + " NOT NULL" +
                ")"
        );
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseTable.NAME, null, getContentValues(expense));
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
        onCreate(sqLiteDatabase);
    }
}