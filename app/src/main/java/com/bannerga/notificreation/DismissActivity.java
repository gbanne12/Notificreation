package com.bannerga.notificreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bannerga.notificreation.notification.ServiceNotification;

public class DismissActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent serviceIntent = new Intent(this, ServiceNotification.class);
        stopService(serviceIntent);
        this.finishAffinity();
        Toast.makeText(this, "Service closed", Toast.LENGTH_SHORT).show();
    }

}
