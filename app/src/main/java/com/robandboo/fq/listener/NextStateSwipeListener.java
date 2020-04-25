package com.robandboo.fq.listener;

import android.view.View;

import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.util.swipe.OnSwipeListener;

public class NextStateSwipeListener implements OnSwipeListener {
    private ChainManager chainManager;

    public NextStateSwipeListener(ChainManager chainManager) {
        this.chainManager = chainManager;
    }

    @Override
    public void onSwipe(View view) {
        chainManager.next();
    }
}
