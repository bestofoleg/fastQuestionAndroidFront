package com.robandboo.fq.chain.state;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.R;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.util.validation.AnswerValidation;

public class AnswerToQuestionPageState implements IState {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private AnswerValidation answerValidation;

    private int questionNumber;

    private int questionsQuantity;

    public AnswerToQuestionPageState(
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AppCompatActivity appCompatActivity,
            int questionNumber, int questionsQuantity) {
        this.questionNumber = questionNumber;
        this.questionsQuantity = questionsQuantity;
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        answerValidation = new AnswerValidation(appCompatActivity);
    }

    @Override
    public void start() {
        answerToQuestionsPresenter.clearAnswerTextEdit();
        answerToQuestionsPresenter.setQuestionNumber(questionsQuantity - questionNumber + 1);
        answerToQuestionsPresenter.loadRandomQuestion();
        answerToQuestionsPresenter.focus();
    }

    @Override
    public boolean work() {
        answerToQuestionsPresenter.sendAnswer(answerValidation);
        return answerValidation.validate();
    }

    @Override
    public void finish() {
        MainActivity.changeBackground();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
