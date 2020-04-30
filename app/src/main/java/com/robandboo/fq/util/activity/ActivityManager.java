package com.robandboo.fq.util.activity;

import android.app.Activity;
import android.content.Intent;
import android.util.Pair;

public class ActivityManager {
    private Activity firstActivity;

    public ActivityManager(Activity firstActivity) {
        this.firstActivity = firstActivity;
    }

    public void changeActivityTo(Class<? extends Activity> newActivityClass) {
        Intent intent = new Intent(firstActivity, newActivityClass);
        firstActivity.startActivity(intent);
    }

    public void changeActivityTo(
            Class<? extends Activity> newActivityClass, Pair<String, String> data
    ) {
        Intent intent = new Intent(firstActivity, newActivityClass);
        intent.putExtra(data.first, data.second);
        firstActivity.startActivity(intent);
    }

    public Intent changeActivityToNewWithResult(
            Class<? extends Activity> newActivityClass, Pair<String, String> data
    ) {
        Intent intent = new Intent(firstActivity, newActivityClass);
        intent.putExtra(data.first, data.second);
        firstActivity.startActivity(intent);
        return intent;
    }
}
