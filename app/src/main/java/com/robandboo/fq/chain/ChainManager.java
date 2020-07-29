package com.robandboo.fq.chain;

import com.robandboo.fq.chain.state.IAnimationTransitionState;
import com.robandboo.fq.chain.state.IState;

import java.util.List;

public final class ChainManager {
    private List<IState> states;

    private int statePointer;

    private static ChainManager instance;

    public ChainManager(List <IState> states) {
        this.states = states;
        statePointer = 0;
        states.get(statePointer).start();
        if (states.get(statePointer) instanceof IAnimationTransitionState) {
            states.get(statePointer).work();
            statePointer ++;
            states.get(statePointer).start();
        }
    }

    //TODO: needed to make algorithm clearly
    public void next() {
        if (states.get(statePointer).work()) {
            states.get(statePointer).finish();
            if (states.get((statePointer + 1) % states.size())
                    instanceof IAnimationTransitionState) {
                states.get((statePointer + 1) % states.size()).work();
                statePointer = (statePointer + 2) % (states.size()-1);
            } else {
                statePointer = (statePointer + 1) % (states.size()-1);
            }
            states.get(statePointer).start();
        }
    }
}
