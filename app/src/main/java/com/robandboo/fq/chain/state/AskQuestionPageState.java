package com.robandboo.fq.chain.state;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.chain.bridge.IDataBridgeStart;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.util.validation.QuestionValidation;

import java.io.File;
import java.util.List;

public class AskQuestionPageState implements IState {
    private AskQuestionPresenter askQuestionPresenter;

    private QuestionValidation answerValidation;

    private IDataBridgeStart<Question> questionDataBridgeStart;

    private  AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private IDataBridgeStart<List<File>> fileDataBridge;

    public AskQuestionPageState(
            AskQuestionPresenter askQuestionPresenter,
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AppCompatActivity appCompatActivity,
            IDataBridgeStart<Question> questionIDataBridgeStart,
            IDataBridgeStart<List<File>> fileIDataBridgeStart) {
        this.askQuestionPresenter = askQuestionPresenter;
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        this.questionDataBridgeStart = questionIDataBridgeStart;
        answerValidation = new QuestionValidation(appCompatActivity);
        this.fileDataBridge = fileIDataBridgeStart;
    }

    @Override
    public void start() {
        askQuestionPresenter.focus();
    }

    @Override
    public boolean work() {
        questionDataBridgeStart.setData(askQuestionPresenter.sendQuestion(answerValidation));
        fileDataBridge.setData(askQuestionPresenter.getImageFilesFromAddImagePresenter());
        boolean isValid = true;
        if (!answerValidation.textIsEmpty()) {
            isValid = answerValidation.validate();
        }
        return isValid;
    }

    @Override
    public void finish() {
        askQuestionPresenter.clearQuestionEditText();
        askQuestionPresenter.clearCheckBox();
        askQuestionPresenter.clearRemoveImageButtons();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
