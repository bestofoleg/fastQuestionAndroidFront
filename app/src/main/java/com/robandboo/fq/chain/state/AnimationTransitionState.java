package com.robandboo.fq.chain.state;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.presenter.ILayoutPresenter;

public class AnimationTransitionState implements IAnimationTransitionState {
    private Animation firstAnimation;

    private Animation secondAnimation;

    private ILayoutPresenter firstLayoutPresenter;

    private ILayoutPresenter secondLayoutPresenter;

    private boolean isChangePageTransition;

    public AnimationTransitionState(
            int firstAnimationId,
            int secondAnimationId,
            ILayoutPresenter firstLayoutPresenter,
            ILayoutPresenter secondLayoutPresenter) {

        Context context = (firstLayoutPresenter != null) ?
                firstLayoutPresenter.getRootLayout().getContext() :
                secondLayoutPresenter.getRootLayout().getContext();
        this.firstAnimation = (firstAnimationId >= 0)?
                AnimationUtils.loadAnimation(context, firstAnimationId) : null;
        this.secondAnimation = (secondAnimationId >= 0)?
                AnimationUtils.loadAnimation(context, secondAnimationId) : null;
        this.firstLayoutPresenter = firstLayoutPresenter;
        this.secondLayoutPresenter = secondLayoutPresenter;
        if (firstAnimation != null) {
            firstAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    startSecondAnimIfNeeded();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    public boolean isChangePageTransition() {
        return isChangePageTransition;
    }

    public void setChangePageTransition(boolean changePageTransition) {
        isChangePageTransition = changePageTransition;
    }

    private void startSecondAnimIfNeeded() {
        if (isChangePageTransition) {
            firstLayoutPresenter.setLayoutVisibility(false);
            secondLayoutPresenter.setLayoutVisibility(true);
        }
        if (secondAnimation != null && secondLayoutPresenter != null) {
            secondLayoutPresenter.getRootLayout()
                    .startAnimation(secondAnimation);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public boolean work() {
        if(firstLayoutPresenter != null & firstAnimation != null) {
            firstLayoutPresenter.getRootLayout()
                    .startAnimation(firstAnimation);
        } else {
            startSecondAnimIfNeeded();
        }
        return true;
    }

    @Override
    public void finish() {

    }

    @Override
    public String toString() {
        return "AnimationTransitionState{" +
                firstLayoutPresenter.toString() +
                " to " +
                secondLayoutPresenter.toString() +
                "}";
    }
}
