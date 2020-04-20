package com.robandboo.fq.util.swipe;

public class SwipeSettings {
    private float minSwipeVelocity;

    private float minDistance;

    private float maxDistance;

    private SwipeVector swipeVector;

    public SwipeSettings(float minSwipeVelocity, float minDistance, float maxDistance, SwipeVector swipeVector) {
        this.minSwipeVelocity = minSwipeVelocity;
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.swipeVector = swipeVector;
    }

    public float getMinSwipeVelocity() {
        return minSwipeVelocity;
    }

    public void setMinSwipeVelocity(float minSwipeVelocity) {
        this.minSwipeVelocity = minSwipeVelocity;
    }

    public SwipeVector getSwipeVector() {
        return swipeVector;
    }

    public void setSwipeVector(SwipeVector swipeVector) {
        this.swipeVector = swipeVector;
    }

    public float getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(float minDistance) {
        this.minDistance = minDistance;
    }

    public float getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }
}
