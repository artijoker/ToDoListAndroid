package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int SCHEMA = 1;

    static final String TABLE_TASKS = "tasks";
    static final String TABLE_TASK_LISTS = "taskLists";


    public static final String COLUMN_ID = "_id";


    //taskLists
    public static final String COLUMN_NAME = "name";

    //tasks
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_DATE_WHEN_DONE = "dateWhenDone";
    public static final String COLUMN_IS_COMPLETED = "isCompleted";
    public static final String COLUMN_IS_TAGGED = "isTagged";
    public static final String COLUMN_LIST_ID = "listId";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(
                "CREATE TABLE " + TABLE_TASK_LISTS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT  NOT NULL"
                        + ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_TASKS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_TITLE + " TEXT NOT NULL, "
                        + COLUMN_TEXT + " TEXT, "
                        + COLUMN_END_DATE + " TEXT, "
                        + COLUMN_DATE_WHEN_DONE + " TEXT, "
                        + COLUMN_IS_COMPLETED + " TEXT NOT NULL, "
                        + COLUMN_IS_TAGGED + " TEXT NOT NULL,  "
                        + COLUMN_LIST_ID + " INTEGER NOT NULL,"
                        + " FOREIGN KEY (" + COLUMN_LIST_ID + ") " +
                        "REFERENCES " + TABLE_TASK_LISTS + " (" + COLUMN_ID + " ) ON DELETE CASCADE"
                        + ");"
        );


        db.execSQL("INSERT INTO " + TABLE_TASK_LISTS + " ("
                + COLUMN_NAME + ") " + "VALUES ('Мои задачи');"
        );

    }
    @Override
    public void onOpen(SQLiteDatabase db){
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
