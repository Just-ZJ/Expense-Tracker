package com.zj.android.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable;
import com.zj.android.expensetracker.database.CategoryDbSchema.CategoryTable.Cols;
import com.zj.android.expensetracker.models.Category;

public class CategoryDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "category.db";

    public CategoryDataBase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static ContentValues getContentValues(Category category) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, category.getId().toString());
        values.put(Cols.NAME, category.getName());
        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CategoryTable.NAME + "(" +
                " id integer primary key autoincrement, " +
                Cols.UUID + " not null unique, " +
                Cols.NAME + " varchar(25) not null unique " +
                ")"
        );
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

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryTable.NAME);
        onCreate(sqLiteDatabase);
    }
}