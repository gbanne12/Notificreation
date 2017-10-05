package com.bannerga.notificreation.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.bannerga.notificreation.NotificationActivity;
import com.bannerga.notificreation.R;

import java.util.Random;


public class PlainOldNotification {

    private Context context;

    public PlainOldNotification(Context context) {
        this.context = context;
    }

    public void issueNotification() {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Build notification
        NotificationContent content = NotificationContent.getInstance();
        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(getPendingIntent())
                .setContentTitle(content.getTitle())
                .setContentText(content.getBody())
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(content.getColor())
                .setAutoCancel(true)
                .setOngoing(false);
        setAndroidOBehaviour(notificationManager, builder);
        Notification notification = builder.build();

        notificationManager.notify(getRandomDigit(), notification);
    }

    private void setAndroidOBehaviour(NotificationManager manager, Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_02";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
            builder.setColorized(true);
            builder.setChannelId(CHANNEL_ID);
        }
    }

    private PendingIntent getPendingIntent() {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);

        return PendingIntent.getActivity(context,
                getRandomDigit(),
                notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);
    }

    private int getRandomDigit() {
        Random rand = new Random();
        return Integer.parseInt(Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1));
    }
}
