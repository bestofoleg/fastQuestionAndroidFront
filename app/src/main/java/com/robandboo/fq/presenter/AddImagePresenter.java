package com.robandboo.fq.presenter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;
import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class AddImagePresenter {
    private ImageView imageToSend1;

    private ImageView imageToSend2;

    private Drawable startImage;

    private ImageView removeImage1;

    private ImageView removeImage2;

    private Activity activity;

    private File [] files;

    public AddImagePresenter(
            final ImageView imageToSend1,
            final ImageView imageToSend2,
            final Activity activity) {
        this.imageToSend1 = imageToSend1;
        this.imageToSend2 = imageToSend2;
        CheckBox isVoiterCheckBox = activity.findViewById(R.id.isVoiter);
        isVoiterCheckBox.setVisibility(View.GONE);
        startImage = imageToSend1.getDrawable();
        this.activity = activity;
        files = new File[2];
        this.imageToSend1.setOnClickListener((view) -> {
            Intent fileManagerIntent = new Intent();
            fileManagerIntent.setType("image/*");
            MainActivity.fileManagerReturnResultListener = (intent) -> {
                String path = intent.getStringExtra("imagePath");
                loadImage(imageToSend1, path);
                File file = new File(path);
                files[0] = file;
                dispatchVoiterState(isVoiterCheckBox);
                removeImage1.setVisibility(View.VISIBLE);
            };
            ImagePicker.with(activity)
                    .setMultipleMode(false)
                    .setShowCamera(true)
                    .setCameraOnly(false)
                    .setRequestCode(Config.RC_PICK_IMAGES)
                    .start();
        });
        this.imageToSend2.setOnClickListener((view) -> {
            Intent fileManagerIntent = new Intent();
            fileManagerIntent.setType("image/*");
            MainActivity.fileManagerReturnResultListener = (intent) -> {
                String path = intent.getStringExtra("imagePath");
                loadImage(imageToSend2, path);
                File file = new File(path);
                files[1] = file;
                dispatchVoiterState(isVoiterCheckBox);
                removeImage2.setVisibility(View.VISIBLE);
            };
            ImagePicker.with(activity)
                    .setMultipleMode(false)
                    .setShowCamera(true)
                    .setCameraOnly(false)
                    .setRequestCode(Config.RC_PICK_IMAGES)
                    .start();
        });
        this.removeImage1 = activity.findViewById(R.id.removeImageFromQuestionButton1);
        this.removeImage2 = activity.findViewById(R.id.removeImageFromQuestionButton2);

        removeImage1.setOnClickListener((view) -> {
            clearImage(0);
            removeImage1.setVisibility(View.GONE);
            dispatchVoiterState(isVoiterCheckBox);
        });

        removeImage2.setOnClickListener((view) -> {
            clearImage(1);
            removeImage2.setVisibility(View.GONE);
            dispatchVoiterState(isVoiterCheckBox);
        });
    }

    private void dispatchVoiterState(CheckBox isVoiterCheckBox) {
        if (files[0] != null && files[1] != null) {
            isVoiterCheckBox.setVisibility(View.VISIBLE);
        } else {
            isVoiterCheckBox.setVisibility(View.GONE);
            isVoiterCheckBox.setChecked(false);
        }
    }

    public void setRemoveImageButtonActive(boolean isActive) {
        if (isActive) {
            removeImage1.setVisibility(View.VISIBLE);
            removeImage2.setVisibility(View.VISIBLE);
        } else {
            removeImage1.setVisibility(View.GONE);
            removeImage2.setVisibility(View.GONE);
        }
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

    public void clearImage(int imageIndex) {
        ImageView currentImageView = (imageIndex == 0) ? imageToSend1 : imageToSend2;
        Glide
                .with(currentImageView)
                .load(startImage)
                .into(currentImageView);
        files[imageIndex] = null;
    }

    public List<File> getImageFiles() {
        return Arrays.asList(files);
    }
}
