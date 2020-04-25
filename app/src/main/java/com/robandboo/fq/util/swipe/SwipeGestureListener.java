package com.robandboo.fq.util.swipe;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class SwipeGestureListener implements GestureDetector.OnGestureListener {
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
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent firstEvent, MotionEvent secondEvent, float velocityX, float velocityY){
        SwipeVector swipeVector = swipeSettings.getSwipeVector();

        float diffX = firstEvent.getX() - secondEvent.getX();
        float diffY = firstEvent.getY() - secondEvent.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            //horizontalSwipe
            if (Math.abs(diffX) > swipeSettings.getMinDistance() &&
                    Math.abs(velocityX) > swipeSettings.getMinSwipeVelocity()) {
                if (velocityX * swipeVector.getX() > 0) {
                    onSwipeListener.onSwipe(view);
                }
            }
        } else {
            //verticalSwipe
            if (Math.abs(diffY) > swipeSettings.getMinDistance() &&
                    Math.abs(velocityY) > swipeSettings.getMinSwipeVelocity()) {
                if (velocityY * swipeVector.getY() > 0) {
                    onSwipeListener.onSwipe(view);
                }
            }
        }
        return false;
    }
}
