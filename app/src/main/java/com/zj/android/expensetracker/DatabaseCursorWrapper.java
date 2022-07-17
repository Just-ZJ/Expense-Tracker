package com.zj.android.expensetracker;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.ExpenseDbSchema.ExpenseTable;
import com.zj.android.expensetracker.database.ExpenseToCategoryDbSchema.ExpenseToCategoryTable;
import com.zj.android.expensetracker.models.Category;
import com.zj.android.expensetracker.models.Expense;
import com.zj.android.expensetracker.models.ExpenseToCategory;

import java.util.UUID;

public class DatabaseCursorWrapper extends CursorWrapper {

    public DatabaseCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public int getInt(String columnName) {
        return getInt(getColumnIndex(columnName));
    }

    public double getDouble(String columnName) {
        return getDouble(getColumnIndex(columnName));
    }

    public String getString(String columnName) {
        return getString(getColumnIndex(columnName));
    }

    /*************************** getting tuples from database **********************/
    public Category getCategory() {
        String uuidString = getString(getColumnIndex(CategoryTable.Cols.UUID));
        String title = getString(getColumnIndex(CategoryTable.Cols.NAME));

        Category category = new Category(UUID.fromString(uuidString));
        category.setName(title);

        return category;
    }

    public Expense getExpense() {
        String uuidString = getString(getColumnIndex(ExpenseTable.Cols.UUID));
        String date = getString(getColumnIndex(ExpenseTable.Cols.DATE));
        String details = getString(getColumnIndex(ExpenseTable.Cols.DETAILS));
        String amount = getString(getColumnIndex(ExpenseTable.Cols.AMOUNT));

        Expense expense = new Expense(UUID.fromString(uuidString));
        expense.setDate(new CustomDate(date));
        expense.setDetails(details);
        expense.setAmount(Double.parseDouble(amount));

        return expense;
    }

    public ExpenseToCategory getExpenseToCategory() {
        String uuidString = getString(getColumnIndex(ExpenseToCategoryTable.Cols.UUID));
        String expenseId = getString(getColumnIndex(ExpenseToCategoryTable.Cols.EXPENSE_UUID));
        String categoryId = getString(getColumnIndex(ExpenseToCategoryTable.Cols.CATEGORY_UUID));

        ExpenseToCategory expenseToCategory = new ExpenseToCategory(UUID.fromString(uuidString));
        expenseToCategory.setExpenseId(UUID.fromString(expenseId));
        expenseToCategory.setCategoryId(UUID.fromString(categoryId));

        return expenseToCategory;
    }

}
