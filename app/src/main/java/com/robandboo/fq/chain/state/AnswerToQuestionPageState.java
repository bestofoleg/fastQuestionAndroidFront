package com.robandboo.fq.chain.state;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.MainActivity;
import com.robandboo.fq.chain.ChainManager;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.util.enumeration.QuestionType;
import com.robandboo.fq.util.validation.AnswerValidation;

public class AnswerToQuestionPageState implements IState {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private AnswerValidation answerValidation;

    private int questionNumber;

    private int questionsQuantity;

    private ChainManager chainManager;

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
        answerToQuestionsPresenter.transferQuestionTextViewInLoadState();
        answerToQuestionsPresenter.transferImagesToLoadState();
        answerToQuestionsPresenter.setQuestionNumber(questionsQuantity - questionNumber + 1);
        answerToQuestionsPresenter.setChainManager(chainManager);
        answerToQuestionsPresenter.loadRandomQuestion();
        answerToQuestionsPresenter.setInputAnswerLayoutVisibility(true);
        answerToQuestionsPresenter.setLoadAnswersLayoutVisibility(false);
        answerToQuestionsPresenter.focus();
    }

    @Override
    public boolean work() {
        answerToQuestionsPresenter.sendAnswer(answerValidation);
        Boolean skipOnce = answerToQuestionsPresenter.getSkipValidationOnce();
        answerToQuestionsPresenter.setSkipValidationOnce(Boolean.FALSE);
        boolean valid = this.answerValidation.validateWithoutToast() || skipOnce;
        if (!valid &&
                !QuestionType.VOTE.isA(answerToQuestionsPresenter
                        .getCurrentQuestion().getQuestionType())) {
            answerValidation.validate();
        }
        return valid;
    }

    @Override
    public void finish() {
        answerToQuestionsPresenter.clearAnswerTextEdit();
        MainActivity.changeBackground();
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
