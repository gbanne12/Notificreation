package com.bannerga.notificreation.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bannerga.notificreation.DismissActivity;
import com.bannerga.notificreation.R;

import java.util.Random;

public class ServiceNotification extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Build notification
        NotificationContent content = NotificationContent.getInstance();
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(getPendingIntent())
                .setContentTitle(content.getTitle())
                .setContentText(content.getBody())
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(content.getColor());
        setAndroidOBehaviour(manager, builder);
        Notification notification = builder.build();

        // Start foreground service
        startForeground(getRandomDigits(), notification);

        return START_REDELIVER_INTENT;
    }

    private void setAndroidOBehaviour(NotificationManager manager, Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(
                    "my_channel_01",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
            builder.setColorized(true);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(this, DismissActivity.class);
        notificationIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));

        return PendingIntent.getActivity(this,
                getRandomDigits(),
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private int getRandomDigits() {
        Random rand = new Random();
        return Integer.parseInt(Integer.toString(rand.nextInt(9) + 1)
                        + Integer.toString(rand.nextInt(9) + 1)
                        + Integer.toString(rand.nextInt(9) + 1));
    }
}
