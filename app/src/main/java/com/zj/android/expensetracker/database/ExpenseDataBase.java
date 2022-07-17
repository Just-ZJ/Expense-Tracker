package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.database.ExpenseDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable.Cols;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseToCategoryTable;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;
import com.zj.android.expensetracker.models.ExpenseToCategory;

public class ExpenseDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expense.db";

    public ExpenseDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, expense.getId().toString());
        // date type is stored as String in database - ISO 8601 format
        values.put(Cols.DATE, expense.getDate().toDatabaseString());
        values.put(Cols.DETAILS, expense.getDetails());
        values.put(Cols.AMOUNT, expense.getAmount());
        return values;
    }

    private static ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.UUID, category.getId().toString());
        values.put(CategoryTable.Cols.NAME, category.getName());
        return values;
    }

    private static ContentValues getContentValues(ExpenseToCategory expenseToCategory) {
        ContentValues values = new ContentValues();
        values.put(ExpenseToCategoryTable.Cols.UUID, expenseToCategory.getId().toString());
        values.put(ExpenseToCategoryTable.Cols.EXPENSE_UUID, expenseToCategory.getExpenseId().toString());
        values.put(ExpenseToCategoryTable.Cols.CATEGORY_UUID, expenseToCategory.getCategoryId().toString());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Expense Table
        sqLiteDatabase.execSQL("CREATE TABLE " + ExpenseTable.NAME + "(" +
                " id integer PRIMARY KEY AUTOINCREMENT, " +
                Cols.UUID + " text NOT NULL UNIQUE, " +
                Cols.DATE + " date NOT NULL, " +
                Cols.DETAILS + " text, " +
                Cols.AMOUNT + " NOT NULL" +
                ")"
        );
        // Category Table
        sqLiteDatabase.execSQL("CREATE TABLE " + CategoryTable.NAME + "(" +
                " id integer PRIMARY KEY AUTOINCREMENT, " +
                CategoryTable.Cols.UUID + " text NOT NULL UNIQUE, " +
                CategoryTable.Cols.NAME + " varchar(25) NOT NULL UNIQUE " +
                ")"
        );
        // ExpenseToCategory Table
        sqLiteDatabase.execSQL(
                "CREATE TABLE " + ExpenseToCategoryTable.NAME + "(" +
                        " id integer PRIMARY KEY AUTOINCREMENT, " +
                        ExpenseToCategoryTable.Cols.UUID + " text NOT NULL UNIQUE, " +
                        ExpenseToCategoryTable.Cols.EXPENSE_UUID + " text NOT NULL, " +
                        ExpenseToCategoryTable.Cols.CATEGORY_UUID + " text NOT NULL, " +
                        "FOREIGN KEY (" + ExpenseToCategoryTable.Cols.EXPENSE_UUID + ") REFERENCES " + ExpenseTable.NAME + "(" + ExpenseTable.Cols.UUID + "), " +
                        "FOREIGN KEY (" + ExpenseToCategoryTable.Cols.CATEGORY_UUID + ") REFERENCES " + CategoryTable.NAME + "(" + CategoryTable.Cols.UUID + ") " +
                        ")"
        );
    }

    public void addExpense(Expense expense) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseTable.NAME, null, getContentValues(expense));
        database.close();
    }

    public void addCategory(Category category) {
        SQLiteDatabase database = this.getWritableDatabase();
        try {
            database.insert(CategoryTable.NAME, null, getContentValues(category));
        } catch (SQLiteConstraintException e) {
            // do nothing.
            // TODO: alert user if they added duplicated categories maybe? check if user added using boolean?
        }
        database.close();
    }

    public void addCategory(ExpenseToCategory expenseToCategory) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(ExpenseToCategoryTable.NAME, null, getContentValues(expenseToCategory));
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryTable.NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseToCategoryTable.NAME);
        onCreate(sqLiteDatabase);
    }
}