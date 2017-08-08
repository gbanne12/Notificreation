package com.bannerga.notificreation.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.bannerga.notificreation.DismissActivity;
import com.bannerga.notificreation.NotificationActivity;
import com.bannerga.notificreation.NotificationContent;
import com.bannerga.notificreation.R;

import java.util.Random;


public class PlainOldNotification {

    Context context;
    int id = 5;

    public PlainOldNotification(Context context) {
        this.context = context;
    }

    public void issueNotification() {
        Random rand = new Random();
        id  = Integer.parseInt(
                Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1));

        NotificationContent content = NotificationContent.getInstance();

        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        //notificationIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));

        PendingIntent contentIntent = PendingIntent.getActivity(context,
                001,
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(contentIntent)
                .setContentTitle(content.getTitle())
                .setContentText(content.getBody())
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(context.getResources().getColor(content.getColor()))
                .setAutoCancel(true)
                .setOngoing(false);
        // setNotificationChannel(builder);
        //setColorizedForV26(builder);
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
