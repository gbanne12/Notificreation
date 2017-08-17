package com.bannerga.notificreation.notification;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bannerga.notificreation.DismissActivity;
import com.bannerga.notificreation.R;

import java.util.Random;

public class ServiceNotification extends Service {

    NotificationManager notificationManager;
    int id = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Random rand = new Random();
        id  = Integer.parseInt(
                Integer.toString(rand.nextInt(9) + 1)
                        + Integer.toString(rand.nextInt(9) + 1)
                        + Integer.toString(rand.nextInt(9) + 1));

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationContent content = NotificationContent.getInstance();

        Intent notificationIntent = new Intent(this, DismissActivity.class);
        notificationIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));

        PendingIntent contentIntent = PendingIntent.getActivity(this, id++, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // Build notification

        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle(content.getTitle())
                .setContentText(content.getBody())
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(content.getColor());
        setNotificationChannel(builder);
        setColorizedForV26(builder);
        Notification notification = builder.build();

        // Start foreground service
        startForeground(id++, notification);

        //notificationManager.notify(content.getId(), notification);

        return START_REDELIVER_INTENT;
    }


    private boolean isOnAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private void setNotificationChannel(Notification.Builder builder) {
        if (isOnAndroidO()) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(
                    "my_channel_01",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }
    }

    private void setColorizedForV26(Notification.Builder builder) {
        if (isOnAndroidO()) {
            builder.setColorized(true);
        }
    }
}
