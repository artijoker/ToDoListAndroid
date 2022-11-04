package com.example.todolist;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.todolist.entities.Task;

import java.time.LocalDate;
import java.util.List;

public class Notifier extends Worker {

    private final static int NOTIFICATION_ID = 12345;
    private final static String CHANNEL_ID = "my_channel_01";

    private final Context context;

    public Notifier(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        DatabaseAdapter dbAdapter = new DatabaseAdapter(context);
        dbAdapter.open();

        List<Task> tasks = dbAdapter.getTasksWithNotifyEnd();
        dbAdapter.close();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "channel",
                NotificationManager.IMPORTANCE_HIGH
        );
        channel.setDescription("channel");
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
        channel.setShowBadge(false);
        manager.createNotificationChannel(channel);

        for (Task task: tasks) {
            if (task.getEndDate().minusDays(1) == LocalDate.now()){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Уведомление")
                        .setContentText(task.getTitle());

                manager.notify(NOTIFICATION_ID, builder.build());
            }
            if (LocalDate.now().compareTo(task.getEndDate()) > 0){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Задача не была завершена")
                        .setContentText(task.getTitle());

                manager.notify(NOTIFICATION_ID, builder.build());
            }
        }

        return Result.success();
    }
}
