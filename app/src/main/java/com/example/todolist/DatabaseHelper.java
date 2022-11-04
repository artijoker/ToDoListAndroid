package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todolist.db";
    private static final int SCHEMA = 1;

    static final String TABLE_TASKS = "tasks";
    static final String TABLE_WORKER_TAGS = "workerTasks";
    static final String TABLE_TASK_LISTS = "taskLists";


    public static final String COLUMN_ID = "_id";

    //workerTasks
    public static final String COLUMN_TAG = "tag";

    //taskLists
    public static final String COLUMN_NAME = "name";

    //tasks
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_TEXT = "text";
    public static final String COLUMN_END_DATE = "endDate";
    public static final String COLUMN_IS_COMPLETED = "isCompleted";
    public static final String COLUMN_IS_TAGGED = "isTagged";
    public static final String COLUMN_IS_NOTIFY_END = "isNotifyEnd";
    public static final String COLUMN_LIST_ID = "listId";




    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_WORKER_TAGS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_TAG + " TEXT"
                        + ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_TASK_LISTS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT"
                        + ");"
        );

        db.execSQL(
                "CREATE TABLE " + TABLE_TASKS + " ("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_TITLE + " TEXT, "
                        + COLUMN_TEXT + " TEXT, "
                        + COLUMN_END_DATE + " TEXT, "
                        + COLUMN_IS_COMPLETED + " TEXT, "
                        + COLUMN_IS_TAGGED + " TEXT, "
                        + COLUMN_IS_NOTIFY_END + " TEXT, "
                        + COLUMN_LIST_ID + " INTEGER "
                        + ");"
        );


        db.execSQL("INSERT INTO " + TABLE_TASK_LISTS + " ("
                + COLUMN_NAME + ") " + "VALUES ('Мои задачи');"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
