package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.CustomDate;
import com.zj.android.expensetracker.database.ExpenseDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;

import java.util.Locale;

public class ExpenseDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "expense.db";

    public ExpenseDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(Expense expense) {
        ContentValues values = new ContentValues();
        values.put(ExpenseTable.Cols.UUID, expense.getId().toString());
        // date type is stored as String in database - ISO 8601 format
        values.put(ExpenseTable.Cols.DATE, expense.getDate().toDatabaseString());
        values.put(ExpenseTable.Cols.CATEGORY_UUID, DatabaseAccessor.getCategoryByName(expense.getCategory()).getId().toString());
        values.put(ExpenseTable.Cols.DETAILS, expense.getDetails());
        values.put(ExpenseTable.Cols.AMOUNT, expense.getAmount());
        return values;
    }

    private static ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Cols.UUID, category.getId().toString());
        values.put(CategoryTable.Cols.NAME, category.getName());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Expense Table
        sqLiteDatabase.execSQL("CREATE TABLE " + ExpenseTable.NAME + "(" +
                " id integer PRIMARY KEY AUTOINCREMENT, " +
                ExpenseTable.Cols.UUID + " text NOT NULL UNIQUE, " +
                ExpenseTable.Cols.DATE + " date NOT NULL, " +
                ExpenseTable.Cols.CATEGORY_UUID + " date NOT NULL, " +
                ExpenseTable.Cols.DETAILS + " text, " +
                ExpenseTable.Cols.AMOUNT + " NOT NULL, " +
                "FOREIGN KEY (" + ExpenseTable.Cols.CATEGORY_UUID + ") REFERENCES " + CategoryTable.NAME + "(" + CategoryTable.Cols.UUID + ") " +
                ")"
        );
        // Category Table
        sqLiteDatabase.execSQL("CREATE TABLE " + CategoryTable.NAME + "(" +
                " id integer PRIMARY KEY AUTOINCREMENT, " +
                CategoryTable.Cols.UUID + " text NOT NULL UNIQUE, " +
                CategoryTable.Cols.NAME + " varchar(25) NOT NULL UNIQUE " +
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

    public void databaseSeeder() {
        String[] category = new String[]{"Grocery", "Fuel", "Dining",
                "Subscriptions", "Miscellaneous"};
        for (int year = 2022; year >= 2019; year--) {
            for (int i = 1; i <= 12; i++) {
                for (int j = 0; j < 5; j++) {
                    String date = String.format(Locale.getDefault(), "%d-%02d-%02d 12:00:00", year, i, j);
                    double amt = -100.00 + Math.random() * -500.00;
                    Expense expense = new Expense(new CustomDate(date), category[(int) (Math.random() * 4)], "Some details here.", amt);
                    addExpense(expense);
                }
                String date = String.format(Locale.getDefault(), "%d-%02d-01 12:00:00", year, i);
                Expense expense = new Expense(new CustomDate(date), "Salary", "Some details here.", 2000.00);
                addExpense(expense);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ExpenseTable.NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryTable.NAME);
        onCreate(sqLiteDatabase);
    }
}