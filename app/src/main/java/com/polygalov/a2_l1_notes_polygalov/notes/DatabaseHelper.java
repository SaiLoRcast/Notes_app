package com.polygalov.a2_l1_notes_polygalov.notes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Константин on 23.09.2017.
 */

class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbNotes";
    public static String TABLE_NAME = "notes";
    public static String FIELD_TITLE = "title";
    public static String FIELD_DESCRIPTION = "description";
    public static String FIELD_DATE = "date";
    public static String FIELD_ID = "_id";

    private static final int DATABASE_VERSION = 1;

    public static String CREATE_TABLE_NOTE = "create table " +
            TABLE_NAME + " (" + FIELD_ID +
            " integer primary key autoincrement, " +
            FIELD_TITLE + " text not null, " +
            FIELD_DESCRIPTION + " text not null, " +
            FIELD_DATE + " text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_NOTE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXIST" + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
