package com.bannerga.notificreation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.bannerga.notificreation.notification.NotificationContent;
import com.bannerga.notificreation.notification.PlainOldNotification;
import com.bannerga.notificreation.notification.ServiceNotification;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.Random;

public class NotificationActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText bodyText;
    private RadioGroup colorRadios;
    private CheckBox persistentCheckBox;
    private int colorSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        setContentView(R.layout.activity_notification);
        titleText = findViewById(R.id.titleTextBox);
        bodyText = findViewById(R.id.bodyTextBox);
        colorRadios = findViewById(R.id.radioColor);
        persistentCheckBox = findViewById(R.id.persistentCheckBox);
    }

    public void submitClick(View view) {
        NotificationContent content = NotificationContent.getInstance();
        content.setBody(bodyText.getText().toString());
        content.setTitle(titleText.getText().toString());
        content.setColor(colorSelection);

        Random rand = new Random();
        String id = Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1)
                + Integer.toString(rand.nextInt(9) + 1);
        int notificationId = Integer.parseInt(id);
        content.setId(notificationId);

        if (persistentCheckBox.isChecked()) {
            Intent serviceIntent = new Intent(this, ServiceNotification.class);
            startService(serviceIntent);
        } else {
            PlainOldNotification notification = new PlainOldNotification(this);
            notification.issueNotification();
        }
    }

    public void showColorPalette(View v) {
        new SpectrumDialog.Builder(this)
                .setColors(R.array.many_shades_of_grey)
                .setSelectedColorRes(R.color.orange)
                .setDismissOnColorSelected(true)
                .setFixedColumnCount(4)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            colorSelection = color;
                        }
                    }
                }).build().show(getSupportFragmentManager(), "color palette");
    }

    private void checkFirstRun() {
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true);
        if (isFirstRun) {
            showIntroDialog();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putBoolean("isFirstRun", false)
                    .apply();
        }
    }

    private void showIntroDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.alert_dialog)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.create().show();
    }
}
