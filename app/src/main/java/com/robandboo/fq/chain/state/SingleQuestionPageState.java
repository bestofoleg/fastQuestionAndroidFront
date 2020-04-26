package com.robandboo.fq.chain.state;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.bridge.IDataBridgeFinish;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;

public class SingleQuestionPageState implements IState {
    private SingleQuestionAnswersPresenter singleQuestionAnswersPresenter;

    private AppCompatActivity appCompatActivity;

    private IDataBridgeFinish<Question> questionIDataBridgeFinish;

    public SingleQuestionPageState(
            SingleQuestionAnswersPresenter singleQuestionAnswersPresenter,
            AppCompatActivity appCompatActivity,
            IDataBridgeFinish<Question> questionIDataBridgeFinish) {
        this.singleQuestionAnswersPresenter = singleQuestionAnswersPresenter;
        this.appCompatActivity = appCompatActivity;
        this.questionIDataBridgeFinish = questionIDataBridgeFinish;
    }

    @Override
    public void start() {
        singleQuestionAnswersPresenter.setVisibility(true);
        singleQuestionAnswersPresenter.updateData(questionIDataBridgeFinish.getData());
    }

    @Override
    public boolean work() {
        return true;
    }

    @Override
    public void finish() {
        singleQuestionAnswersPresenter.setVisibility(false);
    }
}
