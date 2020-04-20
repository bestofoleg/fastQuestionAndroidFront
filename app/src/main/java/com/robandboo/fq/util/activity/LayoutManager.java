package com.robandboo.fq.util.activity;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class LayoutManager {
    private List<LinearLayout> layouts;

    public LayoutManager() {
        layouts = new ArrayList<>();
    }

    public void addLayout(@NonNull LinearLayout layout) {
        layouts.add(layout);
    }

    public LinearLayout setActiveOnly(@NonNull String layoutId) {
        LinearLayout result = null;
        for (LinearLayout layout : layouts) {
            if (layout.equals(layout.getId())) {
                layout.setVisibility(View.VISIBLE);
                result = layout;
            } else {
                layout.setVisibility(View.GONE);
            }
        }
        return result;
    }
}
