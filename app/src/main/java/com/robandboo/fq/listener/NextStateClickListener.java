package com.robandboo.fq.listener;

import android.view.View;

import com.robandboo.fq.chain.ChainManager;

public class NextStateClickListener implements View.OnClickListener {
    private ChainManager chainManager;

    public NextStateClickListener(ChainManager chainManager) {
        this.chainManager = chainManager;
    }

    @Override
    public void onClick(View v) {
        chainManager.next();
    }
}
