package com.robandboo.fq.listener;

import android.app.Dialog;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class TouchFullScreenImageControlListener implements View.OnTouchListener {
    private int startTopViewValueY;

    private int startTopViewValueX;

    private Dialog dialog;

    private Point screenSize;

    private MotionEvent.PointerCoords pointerCoordsFinger1;

    private MotionEvent.PointerCoords pointerCoordsFinger2;

    private Point defaultImageSize;

    private ImageView imageView;

    private Point scalePositionPoint;

    public TouchFullScreenImageControlListener(Dialog dialog, ImageView imageView, Point screenSize) {
        this.startTopViewValueY = 0;
        this.dialog = dialog;
        this.screenSize = screenSize;
        pointerCoordsFinger1 = new MotionEvent.PointerCoords();
        pointerCoordsFinger2 = new MotionEvent.PointerCoords();
        defaultImageSize = new Point();
        this.imageView = imageView;
        scalePositionPoint = new Point();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startTopViewValueY = view.getTop();
            startTopViewValueX = view.getLeft();
        }

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (event.getPointerCount() == 2) {
                event.getPointerCoords(0, pointerCoordsFinger1);
                event.getPointerCoords(1, pointerCoordsFinger2);
                defaultImageSize.x = view.getWidth();
                defaultImageSize.y = view.getHeight();
                float scale = getScaleByFingers(pointerCoordsFinger1, pointerCoordsFinger2);
                imageView.setScaleX(1 + scale);
                imageView.setScaleY(1 + scale);
            } else {
                int eventIntY = (int) event.getY();
                int currentPosY = eventIntY - (view.getHeight() / 2);
                view.setTop(currentPosY);
            }
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (Math.abs(startTopViewValueY - view.getTop()) > (screenSize.y / 7)) {
                dialog.cancel();
            } else {
                view.setTop(startTopViewValueY);
            }
            imageView.setScaleX(1);
            imageView.setScaleY(1);
        }
        return true;
    }

    private float getScaleByFingers(MotionEvent.PointerCoords point1, MotionEvent.PointerCoords point2) {
        float distance = (float) Math.sqrt((point1.x - point2.x)*(point1.x - point2.x)
                + (point1.y - point2.y)*(point1.y - point2.y));
        float maxDistance =
                (float) Math.sqrt(screenSize.x*screenSize.x + screenSize.y*screenSize.y);
        return distance / maxDistance;
    }

    private Point getImageScalePositionByFingers(
            MotionEvent.PointerCoords point1, MotionEvent.PointerCoords point2) {
        scalePositionPoint.x = (int) (point1.x - point2.x) / 2;
        scalePositionPoint.y = (int) (point1.y - point2.y) / 2;
        return scalePositionPoint;
    }
}
