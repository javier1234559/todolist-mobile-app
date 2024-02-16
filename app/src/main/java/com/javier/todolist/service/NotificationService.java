package com.javier.todolist.service;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.javier.todolist.database.TaskDAO;
import com.javier.todolist.model.TodoItem;

import java.util.List;

public class NotificationService extends Service {
    private static final int ALARM_REQUEST_CODE = 123;
    private TaskDAO taskDAO;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("SERVICE","RUNNNNN!!!");
//        List<TodoItem> todoItemList = getTodoItemList(); // Replace with your logic to get the list
//
//        for (TodoItem todoItem : todoItemList) {
//            scheduleNotification(todoItem);
//        }

        // Return START_STICKY to ensure the service restarts if killed by the system
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void scheduleNotification(TodoItem todoItem) {
        // Get the due time from the TodoItem
        long dueTimeMillis = todoItem.getDueDate().getTime();

        // Create an Intent for the AlarmReceiver
        Intent intent = new Intent(this, AlarmReceiver.class);

        // Pass the TodoItem details as extras to the receiver
        intent.putExtra("todoItemId", todoItem.getId());
        // ... other details

        // Create a PendingIntent
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, ALARM_REQUEST_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Schedule the alarm
        if (alarmManager != null) {
            alarmManager.set(AlarmManager.RTC_WAKEUP, dueTimeMillis, pendingIntent);
        }
    }


}
