package com.robandboo.fq.chain.state;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.bridge.IDataBridgeFinish;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.presenter.SingleQuestionAnswersPresenter;

import java.io.File;
import java.util.List;

public class SingleQuestionPageState implements IState {
    private SingleQuestionAnswersPresenter singleQuestionAnswersPresenter;

    private AppCompatActivity appCompatActivity;

    private IDataBridgeFinish<Question> questionIDataBridgeFinish;

    private IDataBridgeFinish<List<File>> fileIDataBridgeFinish;

    public SingleQuestionPageState(
            SingleQuestionAnswersPresenter singleQuestionAnswersPresenter,
            AppCompatActivity appCompatActivity,
            IDataBridgeFinish<Question> questionIDataBridgeFinish,
            IDataBridgeFinish<List<File>> filesFinishDataBridge) {
        this.singleQuestionAnswersPresenter = singleQuestionAnswersPresenter;
        this.appCompatActivity = appCompatActivity;
        this.questionIDataBridgeFinish = questionIDataBridgeFinish;
        this.fileIDataBridgeFinish = filesFinishDataBridge;
    }

    @Override
    public void start() {
        singleQuestionAnswersPresenter.viewAllImagesForQuestion(fileIDataBridgeFinish.getData());
        //singleQuestionAnswersPresenter.updateData(questionIDataBridgeFinish.getData());
        singleQuestionAnswersPresenter.focus();
    }

    @Override
    public boolean work() {
        return true;
    }

    @Override
    public void finish() {
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
