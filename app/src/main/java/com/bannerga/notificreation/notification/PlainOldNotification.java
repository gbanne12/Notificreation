package com.bannerga.notificreation.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import com.bannerga.notificreation.NotificationActivity;
import com.bannerga.notificreation.R;

import java.util.Random;


public class PlainOldNotification {

    private Context context;

    public PlainOldNotification(Context context) {
        this.context = context;
    }

    public void issueNotification() {
        Random rand = new Random();
        int id  = Integer.parseInt(
                Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1));

        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                1,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationContent content = NotificationContent.getInstance();
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Log.i("debug","value is " + content.getColor());
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setContentTitle(content.getTitle())
                .setContentText(content.getBody())
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(content.getColor())
                .setAutoCancel(true)
                .setOngoing(false);

        if (isOnAndroidO()) {
            String CHANNEL_ID = "my_channel_02";
            NotificationChannel channel = new NotificationChannel(
                    "my_channel_02",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setColorized(true);
            builder.setChannelId(CHANNEL_ID);
        }
        Notification notification = builder.build();

        notificationManager.notify(id++, notification);
    }

    private boolean isOnAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }
}
