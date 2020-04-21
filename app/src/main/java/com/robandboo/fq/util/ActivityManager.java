package com.robandboo.fq.util;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityManager {
    private AppCompatActivity mainActivity;

    public ActivityManager(AppCompatActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public void changeActivityTo(Class<? extends AppCompatActivity> newActivityClass) {
        Intent intent = new Intent(mainActivity, newActivityClass);
        mainActivity.startActivity(intent);
    }
}
