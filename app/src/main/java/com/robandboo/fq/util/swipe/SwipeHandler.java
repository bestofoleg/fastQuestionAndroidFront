package com.robandboo.fq.util.swipe;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

public class SwipeHandler {
    private SwipeSettings swipeSettings;

    public SwipeHandler(SwipeSettings swipeSettings) {
        this.swipeSettings = swipeSettings;
    }

    public SwipeSettings getSwipeSettings() {
        return swipeSettings;
    }

    public void setSwipeSettings(SwipeSettings swipeSettings) {
        this.swipeSettings = swipeSettings;
    }

    public void setSwipeListener(AppCompatActivity activity, ViewGroup layout, OnSwipeListener swipeListener) {
        SwipeGestureListener swipeGestureListener = new SwipeGestureListener(
                swipeSettings,
                swipeListener,
                layout
        );
        final GestureDetectorCompat gestureDetectorCompat =
                new GestureDetectorCompat(activity, swipeGestureListener);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetectorCompat.onTouchEvent(event);
            }
        });
    }
}
