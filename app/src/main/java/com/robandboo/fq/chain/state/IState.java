package com.robandboo.fq.chain.state;

public interface IState {
    void start();
    boolean work();
    void finish();
}
