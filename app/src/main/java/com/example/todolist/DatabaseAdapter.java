package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.todolist.entities.Task;
import com.example.todolist.entities.TaskList;
import com.example.todolist.entities.WorkerTag;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class DatabaseAdapter {

    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    private final String[] columnsTableTasks = new String[]{
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TITLE,
            DatabaseHelper.COLUMN_TEXT,
            DatabaseHelper.COLUMN_END_DATE,
            DatabaseHelper.COLUMN_IS_COMPLETED,
            DatabaseHelper.COLUMN_IS_TAGGED,
            DatabaseHelper.COLUMN_IS_NOTIFY_END,
            DatabaseHelper.COLUMN_LIST_ID
    };
    private final String[] columnsTableWorkerTags = new String[]{
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_TAG
    };
    private final String[] columnsTableTaskLists = new String[]{
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NAME
    };

    private Cursor getCursorTasksByListId(long listId) {
        String selection = DatabaseHelper.COLUMN_LIST_ID + " = " + listId;
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorCompletedTasksByListId(long listId) {
        String selection = DatabaseHelper.COLUMN_IS_COMPLETED + " = true and "
                + DatabaseHelper.COLUMN_LIST_ID + " = " + listId;
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorNotCompletedTasksByListId(long listId) {
        String selection = DatabaseHelper.COLUMN_IS_COMPLETED + " = false and "
                + DatabaseHelper.COLUMN_LIST_ID + " = " + listId;
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorCompletedTasks() {
        String selection = DatabaseHelper.COLUMN_IS_COMPLETED + " = true";
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorTaggedTasks() {
        String selection = DatabaseHelper.COLUMN_IS_TAGGED + " = true";
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorTasksWithNotifyEnd() {
        String selection = DatabaseHelper.COLUMN_IS_NOTIFY_END + " = true";
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTasks, selection, null, null, null, null);
    }

    private Cursor getCursorAllWorkerTags() {
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableWorkerTags, null, null, null, null, null);
    }

    private Cursor getCursorAllTaskLists() {
        return database.query(DatabaseHelper.TABLE_TASKS, columnsTableTaskLists, null, null, null, null, null);
    }

    private Task cursorToTask(Cursor cursor) {
        int indexColumnId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int indexColumnTitle = cursor.getColumnIndex(DatabaseHelper.COLUMN_TITLE);
        int indexColumnText = cursor.getColumnIndex(DatabaseHelper.COLUMN_TEXT);
        int indexColumnEndDate = cursor.getColumnIndex(DatabaseHelper.COLUMN_END_DATE);
        int indexColumnIsCompleted = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_COMPLETED);
        int indexColumnIsTagged = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_TAGGED);
        int indexColumnIsNotifyEnd = cursor.getColumnIndex(DatabaseHelper.COLUMN_IS_NOTIFY_END);
        int indexColumnListId = cursor.getColumnIndex(DatabaseHelper.COLUMN_LIST_ID);

        int id = cursor.getInt(indexColumnId);
        int listId = cursor.getInt(indexColumnListId);
        String title = cursor.getString(indexColumnTitle);
        String text = cursor.getString(indexColumnText);
        LocalDate endDate = LocalDate.parse(cursor.getString(indexColumnEndDate));
        boolean isCompleted = Boolean.parseBoolean(cursor.getString(indexColumnIsCompleted));
        boolean isTagged = Boolean.parseBoolean(cursor.getString(indexColumnIsTagged));
        boolean isNotifyEnd = Boolean.parseBoolean(cursor.getString(indexColumnIsNotifyEnd));

        return new Task(id, listId, title, text, endDate, isCompleted, isTagged, isNotifyEnd);
    }

    private ContentValues taskToContentValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TITLE, task.getTitle());
        values.put(DatabaseHelper.COLUMN_TEXT, task.getText());
        values.put(DatabaseHelper.COLUMN_END_DATE, task.getEndDate().toString());
        values.put(DatabaseHelper.COLUMN_IS_COMPLETED, Boolean.toString(task.isCompleted()));
        values.put(DatabaseHelper.COLUMN_IS_TAGGED, Boolean.toString(task.isTagged()));
        values.put(DatabaseHelper.COLUMN_IS_NOTIFY_END, Boolean.toString(task.isNotifyEnd()));
        values.put(DatabaseHelper.COLUMN_LIST_ID, Long.toString(task.getListId()));
        return values;
    }

    private WorkerTag cursorToWorkerTag(Cursor cursor) {
        int indexColumnId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int indexColumnTag = cursor.getColumnIndex(DatabaseHelper.COLUMN_TAG);

        int id = cursor.getInt(indexColumnId);
        String tag = cursor.getString(indexColumnTag);

        return new WorkerTag(id, tag);
    }

    private ContentValues workerTagToContentValues(WorkerTag workerTag) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_TAG, workerTag.getTag());
        return values;
    }

    private TaskList cursorToTaskList(Cursor cursor) {
        int indexColumnId = cursor.getColumnIndex(DatabaseHelper.COLUMN_ID);
        int indexColumnName = cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME);

        int id = cursor.getInt(indexColumnId);
        String name = cursor.getString(indexColumnName);

        return new TaskList(id, name);
    }

    private ContentValues taskListToContentValues(TaskList taskList) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, taskList.getName());
        return values;
    }


    public DatabaseAdapter(Context context) {
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter open() {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public List<TaskList> getTaskLists() {
        ArrayList<TaskList> taskLists = new ArrayList<>();
        Cursor cursor = getCursorAllTaskLists();
        while (cursor.moveToNext()) {
            taskLists.add(cursorToTaskList(cursor));
        }
        cursor.close();
        return taskLists;
    }

    public List<WorkerTag> getWorkerTags() {
        ArrayList<WorkerTag> tags = new ArrayList<>();
        Cursor cursor = getCursorAllWorkerTags();
        while (cursor.moveToNext()) {
            tags.add(cursorToWorkerTag(cursor));
        }
        cursor.close();
        return tags;
    }

    public List<Task> getTasksByListId(long listId) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getCursorTasksByListId(listId);
        while (cursor.moveToNext()) {
            tasks.add(cursorToTask(cursor));
        }
        cursor.close();
        return tasks;
    }

    public List<Task> getCompletedTasksByListId(long listId) {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getCursorCompletedTasksByListId(listId);
        while (cursor.moveToNext()) {
            tasks.add(cursorToTask(cursor));
        }
        cursor.close();
        return tasks;
    }

    public List<Task> getTaggedTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getCursorTaggedTasks();
        while (cursor.moveToNext()) {
            tasks.add(cursorToTask(cursor));
        }
        cursor.close();
        return tasks;
    }

    public List<Task> getTasksWithNotifyEnd() {
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = getCursorTasksWithNotifyEnd();
        while (getCursorTasksWithNotifyEnd().moveToNext()) {
            tasks.add(cursorToTask(cursor));
        }
        cursor.close();
        return tasks;
    }

    public long getCountTasks() {
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_TASKS);
    }

    public Task getTask(long id) {
        Task task = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?", DatabaseHelper.TABLE_TASKS, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {

            task = cursorToTask(cursor);
        }
        cursor.close();
        return task;
    }

    public long addTask(Task task) {
        return database.insert(DatabaseHelper.TABLE_TASKS,
                null,
                taskToContentValues(task)
        );
    }

    public long updateTask(Task task) {

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + task.getId();
        return database.update(DatabaseHelper.TABLE_TASKS,
                taskToContentValues(task),
                whereClause,
                null
        );
    }

    public void updateTasks(List<Task> tasks) {

        database.beginTransaction();
        try {
            for (Task task : tasks) {
                String whereClause = DatabaseHelper.COLUMN_ID + "=" + task.getId();
                database.update(DatabaseHelper.TABLE_TASKS,
                        taskToContentValues(task),
                        whereClause,
                        null
                );
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public long deleteTask(long taskId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(taskId)};
        return database.delete(DatabaseHelper.TABLE_TASKS, whereClause, whereArgs);
    }

    public long deleteTasks(List<Task> tasks) {

        String whereClause = "_id = ?";
        String[] whereArgs = tasks.stream()
                .map(t -> Long.toString(t.getId()))
                .toArray(String[]::new);
        return database.delete(DatabaseHelper.TABLE_TASKS, whereClause, whereArgs);
    }

    public long addTaskList(TaskList taskList) {
        return database.insert(DatabaseHelper.TABLE_TASK_LISTS,
                null,
                taskListToContentValues(taskList)
        );
    }

    public long updateTaskList(TaskList taskList) {
        if (taskList.getId() == 1)
            return 0;

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + taskList.getId();
        return database.update(DatabaseHelper.TABLE_TASK_LISTS,
                taskListToContentValues(taskList),
                whereClause,
                null
        );
    }

    public long deleteTaskList(long taskListId) {

        if (taskListId == 1)
            return 0;

        deleteTasks(getTasksByListId(taskListId));
        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(taskListId)};
        return database.delete(DatabaseHelper.TABLE_TASK_LISTS, whereClause, whereArgs);
    }

    public long addWorkerTag(WorkerTag workerTag) {
        return database.insert(DatabaseHelper.TABLE_WORKER_TAGS,
                null,
                workerTagToContentValues(workerTag)
        );
    }

    public long updateWorkerTag(WorkerTag workerTag) {

        String whereClause = DatabaseHelper.COLUMN_ID + "=" + workerTag.getId();
        return database.update(DatabaseHelper.TABLE_WORKER_TAGS,
                workerTagToContentValues(workerTag),
                whereClause,
                null
        );
    }

    public long deleteWorkerTag(long workerTagId) {

        String whereClause = "_id = ?";
        String[] whereArgs = new String[]{String.valueOf(workerTagId)};
        return database.delete(DatabaseHelper.TABLE_WORKER_TAGS, whereClause, whereArgs);
    }

}
