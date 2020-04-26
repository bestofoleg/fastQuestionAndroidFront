package com.robandboo.fq.chain.state;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;
import com.robandboo.fq.chain.bridge.IDataBridgeStart;
import com.robandboo.fq.dto.Question;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.util.validation.QuestionValidation;

public class AskQuestionPageState implements IState {
    private AskQuestionPresenter askQuestionPresenter;

    private QuestionValidation answerValidation;

    private IDataBridgeStart<Question> questionDataBridgeStart;

    private  AnswerToQuestionsPresenter answerToQuestionsPresenter;

    public AskQuestionPageState(
            AskQuestionPresenter askQuestionPresenter,
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AppCompatActivity appCompatActivity,
            IDataBridgeStart<Question> questionIDataBridgeStart) {
        this.askQuestionPresenter = askQuestionPresenter;
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        this.questionDataBridgeStart = questionIDataBridgeStart;
        answerValidation = new QuestionValidation(appCompatActivity);
    }

    @Override
    public void start() {
        answerToQuestionsPresenter.setLayoutVisibility(false);
        askQuestionPresenter.setLayoutVisibility(true);
    }

    @Override
    public boolean work() {
        questionDataBridgeStart.setData(askQuestionPresenter.sendQuestion(answerValidation));
        return answerValidation.validate();
    }

    @Override
    public void finish() {
        askQuestionPresenter.setLayoutVisibility(false);
        askQuestionPresenter.clearQuestionEditText();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
