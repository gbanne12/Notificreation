package com.bannerga.notificreation.notification;

import android.util.Log;

public class NotificationContent {

    private static NotificationContent instance = null;

    private String title = "New Message";
    private String body = "You can customise your own message";
    private int color;
    private int id = 1;
    private boolean persistent;

    private NotificationContent() {
        // Singleton, only allow to instantiate itself
    }

    public static NotificationContent getInstance() {
        if (instance == null) {
            instance = new NotificationContent();
        }
        return instance;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        Log.i("debug", "the value int he singleton is" + id);
        return id;
    }

    public int getColor() {
        return color;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }

}
