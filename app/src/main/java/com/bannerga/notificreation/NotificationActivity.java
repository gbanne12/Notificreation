package com.bannerga.notificreation;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;

public class NotificationActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText bodyText;
    private TextView titleLabel;
    private TextView bodyLabel;
    private RadioGroup colorRadios;
    int id = 5;
    //private CheckBox persistentCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        setContentView(R.layout.activity_notification);
        titleLabel = findViewById(R.id.titleLabel);
        titleText = findViewById(R.id.titleTextBox);
        bodyLabel = findViewById(R.id.bodyLabel);
        bodyText =  findViewById(R.id.bodyTextBox);
        colorRadios = findViewById(R.id.radioColor);
        //persistentCheckBox = findViewById(R.id.persistentCheckBox);
    }

    public void buttonClick(View view) {
        NotificationContent content = NotificationContent.getInstance();
        content.setBody(bodyText.getText().toString());
        content.setTitle(titleText.getText().toString());
        //content.setPersistent(persistentCheckBox.isEnabled());

        Random rand = new Random();
        String id  = Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1);
        int notificationId = Integer.parseInt(id);
        Log.i("debug", "the value in the activity is " + id);
        content.setId(notificationId);

        int selectedId = colorRadios.getCheckedRadioButtonId();

        if (selectedId == R.id.radioButton1) {
            content.setColor(R.color.colorGreen);
        } else if (selectedId == R.id.radioButton2) {
            content.setColor(R.color.colorAmber);
        } else if (selectedId == R.id.radioButton3) {
            content.setColor(R.color.colorRed);
        }

        Intent serviceIntent = new Intent(this, MainService.class);
        startService(serviceIntent);


        ///
        ////
        ///
       secondNotification();

    }

    private void secondNotification() {
        Intent notificationIntent = new Intent(this, DismissActivity.class);
        notificationIntent.setData((Uri.parse("custom://"+System.currentTimeMillis())));

        PendingIntent contentIntent = PendingIntent.getActivity(this,
                001, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        // Build notification
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this)
                .setContentIntent(contentIntent)
                .setContentTitle("hello")
                .setContentText("is it me your looking for?")
                .setSmallIcon(R.mipmap.notification_icon)
                .setColor(getResources().getColor(R.color.colorRed))
                //
                .setAutoCancel(true)
                .setOngoing(true);
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

        // issue notification


        notificationManager.notify(id++, notification);
    }

    private boolean isOnAndroidO() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    private void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun){
            showHelpDialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    private void showHelpDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set dialog message
        alertDialogBuilder
                .setMessage("On Android O, the notification will be added as a service. Tap on the notification to dismiss and end the service")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
