package com.afrometal.radoslaw.doitlater;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by radoslaw on 30.04.17.
 */

public class ToDoDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "ToDo.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ToDoContract.ToDoEntry.TABLE_NAME + " (" +
                    ToDoContract.ToDoEntry._ID + " INTEGER PRIMARY KEY," +
                    ToDoContract.ToDoEntry.COLUMN_NAME_TITLE + " TEXT," +
                    ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS + " TEXT," +
                    ToDoContract.ToDoEntry.COLUMN_NAME_DATE + " TIMESTAMP," +
                    ToDoContract.ToDoEntry.COLUMN_NAME_DUE + " TIMESTAMP)";

    public ToDoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long insert(String title, String details, Long due) {
        long time = System.currentTimeMillis();

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues(4);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DATE, time);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DUE, due);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE, title);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS, details);

        return db.insert(ToDoContract.ToDoEntry.TABLE_NAME, null, values);
    }

    public int update(Long id, String title, String details, Long due) {
        SQLiteDatabase db = getReadableDatabase();

        ContentValues values = new ContentValues(3);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DUE, due);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE, title);
        values.put(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS, details);

        String selection = ToDoContract.ToDoEntry._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        return db.update(
                ToDoContract.ToDoEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public int delete(Long id) {
        // Define 'where' part of query.
        String selection = ToDoContract.ToDoEntry._ID + " = ?";
        // Specify arguments in placeholder order.
        String[] selectionArgs = { id.toString() };
        // Issue SQL statement.
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(ToDoContract.ToDoEntry.TABLE_NAME, selection, selectionArgs);
    }

    public ToDoDetailsItem selectDetails(Long id) {
        String[] projection = {
                ToDoContract.ToDoEntry._ID,
                ToDoContract.ToDoEntry.COLUMN_NAME_TITLE,
                ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS,
                ToDoContract.ToDoEntry.COLUMN_NAME_DUE
        };

        SQLiteDatabase db = getReadableDatabase();

        // Filter results WHERE "id" = index
        String selection = ToDoContract.ToDoEntry._ID + " = ?";
        String[] selectionArgs = { id.toString() };

        Cursor cursor = db.query(
                ToDoContract.ToDoEntry.TABLE_NAME,        // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        cursor.moveToNext();

        String title = cursor.getString(
                cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE));
        String details = cursor.getString(
                cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_DETAILS));
        Long due = cursor.getLong(
                cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_DUE));

        cursor.close();

        return new ToDoDetailsItem(id, title, details, due);
    }

    public ArrayList<ToDoListItem> selectAll() {
        String[] projection = {
                ToDoContract.ToDoEntry._ID,
                ToDoContract.ToDoEntry.COLUMN_NAME_TITLE,
                ToDoContract.ToDoEntry.COLUMN_NAME_DATE,
                ToDoContract.ToDoEntry.COLUMN_NAME_DUE
        };

        String sortOrder =
                ToDoContract.ToDoEntry.COLUMN_NAME_DUE;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(
                ToDoContract.ToDoEntry.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        ArrayList<ToDoListItem> items = new ArrayList<>();

        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry._ID));
            String itemTitle = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_TITLE));
            String itemDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_DATE));
            String itemDue = cursor.getString(
                    cursor.getColumnIndexOrThrow(ToDoContract.ToDoEntry.COLUMN_NAME_DUE));

            items.add(new ToDoListItem(itemId, itemTitle, itemDate, itemDue));
        }
        cursor.close();

        return items;
    }
}
