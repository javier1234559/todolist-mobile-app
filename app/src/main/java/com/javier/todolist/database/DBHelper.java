package com.javier.todolist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Name database
    private static String DB_NAME = "TASKS";
    private static Integer DB_VERSION = 1;

    // Task table
    public static final String TABLE_TASKS = "tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_DUE_DATE = "due_date";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_IS_COMPLETED = "is_completed";

    private static final String CREATE_TABLE_TASKS =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT," +
                    COLUMN_DUE_DATE + " DATE," +
                    COLUMN_CATEGORY + " TEXT,"+
                    COLUMN_IS_COMPLETED + " INTEGER );";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_TASKS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = String.format("DROP TABLE IF EXIST %s",TABLE_TASKS);
        db.execSQL(DROP_TABLE);

        onCreate(db);
    }
}
