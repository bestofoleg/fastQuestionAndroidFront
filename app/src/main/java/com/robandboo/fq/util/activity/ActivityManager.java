package com.robandboo.fq.util.activity;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

public class ActivityManager {
    public void changeActivity(
            AppCompatActivity primaryActivity,
            Class <? extends AppCompatActivity> targetActivityClass
    ) {
        Intent intent = new Intent(primaryActivity, targetActivityClass);
        primaryActivity.startActivity(intent);
    }
}
