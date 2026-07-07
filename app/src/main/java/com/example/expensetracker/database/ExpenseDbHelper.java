package com.example.expensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.expensetracker.model.Expense;

import java.util.ArrayList;

public class ExpenseDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "expenses";

    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_AMOUNT = "amount";
    public static final String COL_CATEGORY = "category";
    public static final String COL_DATE = "date";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_TITLE + " TEXT, " + COL_AMOUNT + " REAL, " + COL_CATEGORY + " TEXT, " + COL_DATE + " INTEGER" + ")";

    public ExpenseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertExpense(Expense expense) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, expense.getTitle());
        values.put(COL_AMOUNT, expense.getAmount());
        values.put(COL_CATEGORY, expense.getCategory());
        values.put(COL_DATE, expense.getDate());

        long result = db.insert(TABLE_NAME, null, values);

        db.close();
        return result;
    }

    public ArrayList<Expense> getAllExpenses() {

        ArrayList<Expense> expenseList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_DATE + " DESC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_TITLE));
                double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AMOUNT));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_CATEGORY));
                long date = cursor.getLong(cursor.getColumnIndexOrThrow(COL_DATE));

                Expense expense = new Expense(id, title, amount, category, date);
                expenseList.add(expense);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return expenseList;
    }

    public int updateExpense(Expense expense) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, expense.getTitle());
        values.put(COL_AMOUNT, expense.getAmount());
        values.put(COL_CATEGORY, expense.getCategory());
        values.put(COL_DATE, expense.getDate());

        int result = db.update(TABLE_NAME, values, COL_ID + "=?", new String[]{String.valueOf(expense.getId())});

        db.close();
        return result;
    }

    public int deleteExpense(int id) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME, COL_ID + "=?", new String[]{String.valueOf(id)});

        db.close();
        return result;
    }
}