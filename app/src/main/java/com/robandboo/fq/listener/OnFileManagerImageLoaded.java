package com.robandboo.fq.listener;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.robandboo.fq.util.activity.OnFileManagerReturnResultListener;

public class OnFileManagerImageLoaded implements OnFileManagerReturnResultListener {
    private ImageView imageToSend;

    private Activity activity;

    public OnFileManagerImageLoaded(ImageView imageToSend, Activity activity) {
        this.imageToSend = imageToSend;
        this.activity = activity;
    }

    @Override
    public void onReturnResult(Intent data) {
        String filepath =
                activity.getIntent().getStringExtra("filepath");
        Toast.makeText(activity, filepath, Toast.LENGTH_SHORT).show();
        Glide
                .with(activity)
                .load(filepath)
                .into(imageToSend);
    }
}
