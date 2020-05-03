package com.robandboo.fq.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.robandboo.fq.MainActivity;

import java.io.IOException;

public class AddImagePresenter {
    private ImageView imageToSend1;

    private ImageView imageToSend2;

    private Activity activity;

    public AddImagePresenter(
            final ImageView imageToSend1,
            final ImageView imageToSend2,
            final Activity activity) {
        this.imageToSend1 = imageToSend1;
        this.imageToSend2 = imageToSend2;
        this.activity = activity;
        this.imageToSend1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileManagerIntent = new Intent();
                fileManagerIntent.setType("image/*");
                MainActivity.fileManagerReturnResultListener = (intent) -> {
                    loadImage(imageToSend1, intent.getStringExtra("imagePath"));
                };
                ImagePicker.with(activity)
                        .setMultipleMode(false)
                        .setShowCamera(true)
                        .setCameraOnly(false)
                        .setRequestCode(Config.RC_PICK_IMAGES)
                        .start();
            }
        });
        this.imageToSend2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fileManagerIntent = new Intent();
                fileManagerIntent.setType("image/*");
                MainActivity.fileManagerReturnResultListener = (intent) -> {
                    loadImage(imageToSend2, intent.getStringExtra("imagePath"));
                };
                ImagePicker.with(activity)
                        .setMultipleMode(false)
                        .setShowCamera(true)
                        .setCameraOnly(false)
                        .setRequestCode(Config.RC_PICK_IMAGES)
                        .start();
            }
        });
    }

    private void loadImage(ImageView image, String path) {
        Uri uri = Uri.parse("file:///" + path);
        try {
            Bitmap bitmap = MediaStore.Images.Media
                    .getBitmap(activity.getContentResolver(), uri);
            Glide
                    .with(activity)
                    .load(bitmap)
                    .into(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}