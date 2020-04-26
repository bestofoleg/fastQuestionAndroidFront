package com.robandboo.fq.chain;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;
import com.robandboo.fq.chain.state.IState;

import java.util.List;

public class ChainManager {
    private List<IState> states;

    private int statePointer;

    public ChainManager(List <IState> states) {
        this.states = states;
        statePointer = 0;
        states.get(statePointer).start();
    }

    public void next() {
        if (states.get(statePointer).work()) {
            states.get(statePointer).finish();
            statePointer = (statePointer + 1) % states.size();
            states.get(statePointer).start();
        }
    }
}
