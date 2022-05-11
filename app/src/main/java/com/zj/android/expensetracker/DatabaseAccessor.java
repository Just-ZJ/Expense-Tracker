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
import com.zj.android.expensetracker.models.ExpenseToCategory;

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

//    private static DatabaseCursorWrapper queryExpenseYear(String sql, String[] whereArgs) {
//        Cursor cursor = mExpenseDataBase.rawQuery(sql, whereArgs);
//        return new DatabaseCursorWrapper(cursor);
//    }

    private static DatabaseCursorWrapper queryCategory(String whereClause, String[] whereArgs) {
        Cursor cursor = mCategoryDataBase.query(CategoryTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new DatabaseCursorWrapper(cursor);
    }

    private static DatabaseCursorWrapper queryExpenseToCategory(String whereClause, String[] whereArgs) {
        Cursor cursor = mExpenseToCategoryDataBase.query(ExpenseToCategoryTable.NAME, null, whereClause,
                whereArgs, null, null, null);
        return new DatabaseCursorWrapper(cursor);
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

//    public static List<String> getYears(){
//        List<String> years = new ArrayList<>();
//        String sql = "SELECT * FROM ( SELECT distinct " + ExpenseTable.Cols.DATE + " FROM " + ExpenseTable.NAME + ") ";
//        String[] whereArgs = new String[]{};
//        DatabaseCursorWrapper cursor = queryExpenseYear(sql, whereArgs);
//
//        try {
//            cursor.moveToFirst();
//            int i = 0;
//            while (!cursor.isAfterLast()) {
//                String date = cursor.getString(cursor.getColumnIndex(ExpenseTable.Cols.DATE));
//                String year = date.substring(date.length() - 4);
//                Log.i("tryout1", "getYears: " + year);
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//        return years;
//    }

    public static Category getCategoryByUUID(String uuid) {
        DatabaseCursorWrapper cursor = queryCategory(CategoryTable.Cols.UUID + " =? ", new String[]{uuid});
        cursor.moveToFirst();
        return cursor.getCategory();
    }

    public static Category getCategoryByName(String name) {
        DatabaseCursorWrapper cursor = queryCategory(CategoryTable.Cols.NAME + " =? ", new String[]{name});
        cursor.moveToFirst();
        return cursor.getCategory();
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

    public static void removeCategory(String name) {
        String whereClause = CategoryTable.Cols.NAME + " = ?";
        String[] whereArgs = new String[]{name};
        mCategoryDataBase.delete(CategoryTable.NAME, whereClause, whereArgs);
    }

    public static String getExpenseCategories(Expense expense) {
        String categories = "";
        String whereClause = ExpenseToCategoryTable.Cols.EXPENSE_UUID + " = ?";
        String[] whereArgs = new String[]{expense.getId().toString()};
        DatabaseCursorWrapper cursor = queryExpenseToCategory(whereClause, whereArgs);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (!cursor.isFirst()) {
                    categories += ", ";
                }
                ExpenseToCategory expenseToCategory = cursor.getExpenseToCategory();
                categories += getCategoryByUUID(expenseToCategory.getCategoryId().toString()).getName();
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return categories;
    }

}
