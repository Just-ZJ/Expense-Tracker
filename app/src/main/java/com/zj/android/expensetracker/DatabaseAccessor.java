package com.zj.android.expensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zj.android.expensetracker.database.CategoryDataBase;
import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDataBase;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.database.ExpenseToCategoryDataBase;
import com.zj.android.expensetracker.database.ExpenseToCategoryDbSchema.ExpenseToCategoryTable;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessor {

    private static SQLiteDatabase mCategoryDataBase;
    private static SQLiteDatabase mExpenseDataBase;
    private static SQLiteDatabase mExpenseToCategoryDataBase;
    private final Context mContext;

    public DatabaseAccessor(Context context) {
        mContext = context.getApplicationContext();
        mCategoryDataBase = new CategoryDataBase(mContext).getReadableDatabase();
        mExpenseDataBase = new ExpenseDataBase(mContext).getReadableDatabase();
        mExpenseToCategoryDataBase = new ExpenseToCategoryDataBase(mContext).getReadableDatabase();
    }

    private static DatabaseCursorWrapper queryExpense(String whereClause, String[] whereArgs) {
        Cursor cursor = mExpenseDataBase.query(ExpenseTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }

    private static DatabaseCursorWrapper queryCategory(String whereClause, String[] whereArgs) {
        Cursor cursor = mCategoryDataBase.query(CategoryTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }

    public static void removeCategory(String name) {
        String whereClause = CategoryTable.Cols.NAME + " = ?";
        String[] whereArgs = new String[]{name};
        mCategoryDataBase.delete(CategoryTable.NAME, whereClause, whereArgs);
    }

    public static void removeExpense(Expense expense) {
        String whereClause = ExpenseTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{expense.getId().toString()};
        mExpenseDataBase.delete(ExpenseTable.NAME, whereClause, whereArgs);
    }

    public static List<Expense> getExpenses() {
        List<Expense> expenses = new ArrayList<>();
        DatabaseCursorWrapper cursor = queryExpense(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                expenses.add(cursor.getExpense());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return expenses;
    }

    public static int getCategoriesCount() {
        return queryCategory(null, null).getCount();
    }

    public static String[] getCategories() {
        String[] categories = new String[getCategoriesCount()];
        DatabaseCursorWrapper cursor = queryCategory(null, null);
        try {
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                Category tmp = cursor.getCategory();
                categories[i] = tmp.getName();
                i++;
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return categories;
    }

    private DatabaseCursorWrapper queryExpenseToCategory(String whereClause, String[] whereArgs) {
        Cursor cursor = mExpenseToCategoryDataBase.query(ExpenseToCategoryTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }


}
