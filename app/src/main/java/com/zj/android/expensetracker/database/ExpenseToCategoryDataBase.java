package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.database.ExpenseToCategoryDbSchema.ExpenseToCategoryTable;
import com.zj.android.expensetracker.database.ExpenseToCategoryDbSchema.ExpenseToCategoryTable.Cols;
import com.zj.android.expensetracker.models.ExpenseToCategory;


public class ExpenseToCategoryDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expenseToCategory.db";

    public ExpenseToCategoryDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(ExpenseToCategory expenseToCategory) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, expenseToCategory.getId().toString());
        values.put(Cols.EXPENSE_UUID, expenseToCategory.getExpenseId().toString());
        values.put(Cols.CATEGORY_UUID, expenseToCategory.getCategoryId().toString());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ExpenseToCategoryTable.NAME + "(" +
                " id integer PRIMARY KEY autoincrement, " +
                Cols.UUID + " text NOT NULL UNIQUE, " +
                Cols.EXPENSE_UUID + " text NOT NULL, " +
                Cols.CATEGORY_UUID + " text NOT NULL, " +
                "FOREIGN KEY (" + Cols.EXPENSE_UUID + ") REFERENCES " + ExpenseTable.NAME + "(" + ExpenseTable.Cols.UUID + "), " +
                "FOREIGN KEY (" + Cols.CATEGORY_UUID + ") REFERENCES " + CategoryTable.NAME + "(" + CategoryTable.Cols.UUID + ") " +
                ")"
        );
    }

    public void addCategory(ExpenseToCategory expenseToCategory) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseToCategoryTable.NAME, null, getContentValues(expenseToCategory));
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseToCategoryTable.NAME);
        onCreate(sqLiteDatabase);
    }
}