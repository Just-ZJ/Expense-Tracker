package com.zj.android.expensetracker.database;

import static com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable.Cols;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable;
import com.zj.android.expensetracker.models.Category;

public class CategoryDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "category.db";

    public CategoryDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(Cols.NAME, category.getName());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CategoryTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                Cols.NAME +
                ")"
        );
    }

    public void addCategory(Category category) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(CategoryTable.NAME, null, getContentValues(category));
        database.close();
    }

    public void updateCategory() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    public void removeCategory() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryTable.NAME);
        onCreate(sqLiteDatabase);
    }
}