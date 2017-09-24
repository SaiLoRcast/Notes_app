package com.polygalov.a2_l1_notes_polygalov.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Константин on 23.09.2017.
 */

public class NoteHelper {

    private static String DATABASE_TABLE = DatabaseHelper.TABLE_NAME;
    private Context context;
    private DatabaseHelper databaseHelper;

    private SQLiteDatabase database;

    public NoteHelper(Context context) {
        this.context = context;
    }

    public NoteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        database = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        databaseHelper.close();
    }

    public Cursor searchQuery(String title) {
        return database.rawQuery("SELECT * FROM" + DATABASE_TABLE + " WHERE " +
                DatabaseHelper.FIELD_TITLE + " LIKE '%" + title + "%'", null);
    }

    public ArrayList<Notes> getSearchResult(String keyword) {
        ArrayList<Notes> arrayList = new ArrayList<Notes>();
        Cursor cursor = searchQuery(keyword);
        cursor.moveToFirst();
        Notes notes;
        if (cursor.getCount() > 0) {
            do {
                notes = new Notes();
                notes.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                notes.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                notes.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DESCRIPTION)));
                notes.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DATE)));
                arrayList.add(notes);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public String getData(String data) {
        String result = "";
        Cursor cursor = searchQuery(data);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            result = cursor.getString(2);
            for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        }
        cursor.close();
        return result;
    }

    public Cursor queryAllData() {
        return database.rawQuery("SELECT * FROM " + DATABASE_TABLE + " ORDER BY " +
                DatabaseHelper.FIELD_ID + " DESC", null);
    }

    public ArrayList<Notes> getAllData() {
        ArrayList<Notes> arrayList = new ArrayList<Notes>();
        Cursor cursor = queryAllData();
        cursor.moveToFirst();
        Notes notes;
        if (cursor.getCount() > 0) {
            do {
                notes = new Notes();
                notes.setId(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_ID)));
                notes.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_TITLE)));
                notes.setMessage(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DESCRIPTION)));
                notes.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIELD_DATE)));
                arrayList.add(notes);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    public long insert(Notes notes) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DatabaseHelper.FIELD_TITLE, notes.getTitle());
        initialValues.put(DatabaseHelper.FIELD_DESCRIPTION, notes.getDescription());
        initialValues.put(DatabaseHelper.FIELD_DATE, notes.getDate());
        return database.insert(DATABASE_TABLE, null, initialValues);
    }

    public void update(Notes notes) {
        ContentValues args = new ContentValues();
        args.put(DatabaseHelper.FIELD_TITLE, notes.getTitle());
        args.put(DatabaseHelper.FIELD_DESCRIPTION, notes.getDescription());
        args.put(DatabaseHelper.FIELD_DATE, notes.getDate());
        database.update(DATABASE_TABLE, args, DatabaseHelper.FIELD_ID + "= '" + notes.getId() + "'", null);
    }

    public void delete(int id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.FIELD_ID + " = '" + id + "'", null);

    }

}

