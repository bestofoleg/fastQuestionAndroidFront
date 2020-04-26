package com.robandboo.fq.chain.state;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.util.validation.AnswerValidation;
import com.robandboo.fq.util.validation.QuestionValidation;

public class AnswerToQuestionPageState implements IState {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private AnswerValidation answerValidation;

    public AnswerToQuestionPageState(
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AppCompatActivity appCompatActivity) {
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        answerValidation = new AnswerValidation(appCompatActivity);
    }

    @Override
    public void start() {
        answerToQuestionsPresenter.setLayoutVisibility(true);
        answerToQuestionsPresenter.loadRandomQuestion();
    }

    @Override
    public boolean work() {
        answerToQuestionsPresenter.sendAnswer(answerValidation);
        return answerValidation.validate();
    }

    @Override
    public void finish() {
        answerToQuestionsPresenter.clearAnswerTextEdit();
        answerToQuestionsPresenter.setLayoutVisibility(false);
    }

    @Override
    public String toString() {
        return "AnswerToQuestionPageState";
    }
}
