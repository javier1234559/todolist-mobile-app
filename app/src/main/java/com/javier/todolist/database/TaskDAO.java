package com.javier.todolist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.javier.todolist.model.TodoItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskDAO {

    private static TaskDAO instance;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private TaskDAO(Context context) {
        this.dbHelper = new DBHelper(context);
        open();
    }

    public static synchronized TaskDAO getInstance(Context context) {
        if (instance == null) {
            instance = new TaskDAO(context.getApplicationContext());
        }
        return instance;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public TodoItem getTaskById(long taskId) {
        TodoItem task = null;
        String selection = DBHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(taskId)};

        Cursor cursor = database.query(
                DBHelper.TABLE_TASKS,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            task = new TodoItem();
            task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION)));
            task.setDueDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DUE_DATE))));
            task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CATEGORY)));
            int isCompletedInt = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IS_COMPLETED));
            task.setCompleted(isCompletedInt == 1);

            cursor.close();
        }

        return task;
    }

    public long createTask(TodoItem task) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, task.getTitle());
        values.put(DBHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(DBHelper.COLUMN_CATEGORY, task.getCategory());
        values.put(DBHelper.COLUMN_DUE_DATE, task.getDueDate().getTime());
        values.put(DBHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);
        return database.insert(DBHelper.TABLE_TASKS, null, values);
    }

    public long updateTask(TodoItem task) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TITLE, task.getTitle());
        values.put(DBHelper.COLUMN_DESCRIPTION, task.getDescription());
        values.put(DBHelper.COLUMN_CATEGORY, task.getCategory());
        values.put(DBHelper.COLUMN_DUE_DATE, task.getDueDate().getTime());
        values.put(DBHelper.COLUMN_IS_COMPLETED, task.isCompleted() ? 1 : 0);

        return database.update(DBHelper.TABLE_TASKS, values, DBHelper.COLUMN_ID + " " + task.getId(), null);
    }

    public void delete(long _id) {
        database.delete(DBHelper.TABLE_TASKS, DBHelper.COLUMN_ID + "=" + _id, null);
    }

    public List<TodoItem> getAllTasks() {
        List<TodoItem> list = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_TASKS, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                TodoItem task = new TodoItem();
                task.setId((cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_ID))));
                task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_TITLE)));
                task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION)));
                task.setDueDate(new Date(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DUE_DATE))));
                task.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_CATEGORY)));

                int isCompletetedInt = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_IS_COMPLETED));
                if (isCompletetedInt == 1) task.setCompleted(true);
                else task.setCompleted(false);

                list.add(task);
            } while (cursor.moveToNext());

            cursor.close();
        }
        return list;
    }
}
