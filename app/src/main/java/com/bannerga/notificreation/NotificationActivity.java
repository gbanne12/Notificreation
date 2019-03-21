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
import android.widget.ImageView;
import android.widget.Switch;

import com.bannerga.notificreation.notification.NotificationContent;
import com.bannerga.notificreation.notification.PlainOldNotification;
import com.bannerga.notificreation.notification.ServiceNotification;
import com.thebluealliance.spectrum.SpectrumDialog;

import java.util.Random;

public class NotificationActivity extends AppCompatActivity {

    private EditText titleText;
    private EditText bodyText;
    private CheckBox persistentCheckBox;
    private Switch persistentSwitch;
    private ImageView paletteIcon;

    private int colorSelection;
    private int previousColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkFirstRun();
        setContentView(R.layout.activity_notification);
        titleText = findViewById(R.id.titleTextBox);
        bodyText = findViewById(R.id.bodyTextBox);

        persistentSwitch = findViewById(R.id.persistentSwitch);
        paletteIcon = findViewById(R.id.colorpicker);
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

        if (persistentSwitch.isChecked()) {
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
                .setSelectedColorRes(R.color.white)
                .setDismissOnColorSelected(true)
                .setFixedColumnCount(4)
                .setOnColorSelectedListener(new SpectrumDialog.OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(boolean positiveResult, @ColorInt int color) {
                        if (positiveResult) {
                            colorSelection = color;
                            paletteIcon.setColorFilter(colorSelection);
                        }
                    }
                }).build().show(getSupportFragmentManager(), "color palette");

        paletteIcon.setColorFilter(colorSelection);
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
