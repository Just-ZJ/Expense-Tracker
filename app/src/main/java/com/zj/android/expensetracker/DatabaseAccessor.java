package com.zj.android.expensetracker;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zj.android.expensetracker.database.ExpenseDataBase;
import com.zj.android.expensetracker.database.ExpenseDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccessor {

    private static SQLiteDatabase mDataBase;
    private final Context mContext;

    public DatabaseAccessor(Context context) {
        mContext = context.getApplicationContext();
        mDataBase = new ExpenseDataBase(mContext).getReadableDatabase();
    }

    private static DatabaseCursorWrapper query(String sql, String[] whereArgs) {
        Cursor cursor = mDataBase.rawQuery(sql, whereArgs);
        return new DatabaseCursorWrapper(cursor);
    }

    public static void removeExpense(Expense expense) {
        String whereClause = ExpenseTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{expense.getId().toString()};
        mDataBase.delete(ExpenseTable.NAME, whereClause, whereArgs);
    }

    /**
     * Get the total amount for all expenses in a particular month or year
     *
     * Example SQL Query:
     * SELECT SUM(amount) FROM expenses where strftime('%Y-%m',date) ='2021-07'
     *
     * @param period in the form of "YYYY-MM" or "YYYY"
     * @return the total amount of @period
     */
    public static float getExpenseAmount(String period) {
        String sql = "SELECT SUM(" + ExpenseTable.Cols.AMOUNT + ") as TotalAmount"
                + " FROM " + ExpenseTable.NAME
                + " WHERE strftime('%Y-%m', " + ExpenseTable.Cols.DATE + ") = ?";
        String[] whereArgs = new String[]{period};
        DatabaseCursorWrapper cursor = query(sql, whereArgs);

        try {
            cursor.moveToFirst();
            return (float) cursor.getDouble("TotalAmount");
        } finally {
            cursor.close();
        }
    }

    /**
     * @return List<Expense> of all expenses in the database
     */
    public static List<Expense> getExpenses(String whereClause) {
        List<Expense> expenses = new ArrayList<>();
        String sql = "SELECT * FROM " + ExpenseTable.NAME + whereClause;
        DatabaseCursorWrapper cursor = query(sql, null);
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

    /**
     * @return List<String> of unique years from all of the expenses in the database
     */
    public static List<String> getYears() {
        List<String> years = new ArrayList<>();
        String sql = "SELECT DISTINCT strftime('%Y', " + ExpenseTable.Cols.DATE + ") as Year"
                + " FROM " + ExpenseTable.NAME
                + " ORDER BY Year ASC";
        DatabaseCursorWrapper cursor = query(sql, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                years.add(cursor.getString("Year"));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return years;
    }

    /**
     * Gets all unique months & year of all expenses in the database
     *
     * @return List<String> of unique month & years from all of the expenses in the database
     */
    public static List<String> getUniqueMonthYear() {
        List<String> monthYear = new ArrayList<>();
        String sql = "SELECT DISTINCT strftime('%Y-%m', " + ExpenseTable.Cols.DATE + ") as MonthYear" +
                " FROM " + ExpenseTable.NAME +
                " ORDER BY MonthYear DESC";
        DatabaseCursorWrapper cursor = query(sql, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                monthYear.add(cursor.getString("MonthYear"));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return monthYear;
    }

    /**
     * Gets the # of expenses in a particular @period
     * <p>
     * Example of SQL Query:
     * SELECT COUNT(DATE) FROM expenses WHERE strftime('%Y-%m',date) = '2022-07'
     *
     * @param period a string in the form of '2022-07'
     * @return # of expense in @period
     */
    public static int getMonthYearCount(String period) {
        String sql = "SELECT COUNT(" + ExpenseTable.Cols.DATE + ") as MonthYearCount" +
                " FROM " + ExpenseTable.NAME +
                " WHERE strftime('%Y-%m', " + ExpenseTable.Cols.DATE + ") = ?";
        String[] whereArgs = new String[]{period};
        DatabaseCursorWrapper cursor = query(sql, whereArgs);
        try {
            cursor.moveToFirst();
            return cursor.getInt("MonthYearCount");
        } finally {
            cursor.close();
        }
    }


    public static Category getCategoryByUUID(String uuid) {
        String sql = "SELECT * FROM " + CategoryTable.NAME +
                " WHERE " + CategoryTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[]{uuid};
        DatabaseCursorWrapper cursor = query(sql, whereArgs);
        cursor.moveToFirst();
        return cursor.getCategory();
    }

    public static Category getCategoryByName(String name) {
        String sql = "SELECT * FROM " + CategoryTable.NAME +
                " WHERE " + CategoryTable.Cols.NAME + " = ?";
        String[] whereArgs = new String[]{name};
        DatabaseCursorWrapper cursor = query(sql, whereArgs);
        cursor.moveToFirst();
        return cursor.getCategory();
    }

    public static int getCategoriesCount() {
        String sql = "SELECT * FROM " + CategoryTable.NAME;
        return query(sql, null).getCount();
    }

    public static String[] getCategories() {
        String[] categories = new String[getCategoriesCount()];
        String sql = "SELECT * FROM " + CategoryTable.NAME;
        DatabaseCursorWrapper cursor = query(sql, null);
        try {
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()) {
                Category tmp = cursor.getCategory();
                categories[i++] = tmp.getName();
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
        mDataBase.delete(CategoryTable.NAME, whereClause, whereArgs);
    }

}
