package com.robandboo.fq.presenter;

import android.view.View;

public interface ILayoutPresenter <T extends View> {
    T getRootLayout();
    void setLayoutVisibility(boolean visibility);
    void focus();
}
