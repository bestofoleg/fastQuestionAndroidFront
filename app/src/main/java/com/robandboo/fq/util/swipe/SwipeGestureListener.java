package com.robandboo.fq.util.swipe;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
    private SwipeSettings swipeSettings;

    private OnSwipeListener onSwipeListener;

    private ViewGroup view;

    public SwipeGestureListener(SwipeSettings swipeSettings, OnSwipeListener onSwipeListener, ViewGroup view) {
        this.swipeSettings = swipeSettings;
        this.onSwipeListener = onSwipeListener;
        this.view = view;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent firstEvent, MotionEvent secondEvent, float velocityX, float velocityY){
        SwipeVector swipeVector = swipeSettings.getSwipeVector();

        if (velocityX * swipeVector.getX() > swipeSettings.getMinSwipeVelocity()) {
            if (Math.abs(firstEvent.getX() - secondEvent.getX()) > swipeSettings.getMinDistance() &&
                    Math.abs(firstEvent.getX() - secondEvent.getX()) < swipeSettings.getMaxDistance()) {
                onSwipeListener.onSwipe(view);
            }
        }
        if (velocityY * swipeVector.getY() > swipeSettings.getMinSwipeVelocity()) {
            if (Math.abs(firstEvent.getY() - secondEvent.getY()) > swipeSettings.getMinDistance() &&
                    Math.abs(firstEvent.getY() - secondEvent.getY()) < swipeSettings.getMaxDistance()) {
                onSwipeListener.onSwipe(view);
            }
        }
        return false;
    }
}
