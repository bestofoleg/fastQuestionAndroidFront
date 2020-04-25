package com.robandboo.fq.chain;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;
import com.robandboo.fq.util.validation.Validation;

public class ChainManager {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private AskQuestionPresenter askQuestionPresenter;

    private int maxQuestions;

    private int maxAsks;

    private int questionCounter;

    private int askCounter;

    private Validation validation;

    public ChainManager(
            AppCompatActivity appCompatActivity,
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AskQuestionPresenter askQuestionPresenter,
            int maxQuestions,
            int maxAsks
    ) {
        this.validation = new Validation(appCompatActivity);
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        this.askQuestionPresenter = askQuestionPresenter;
        this.maxAsks = maxAsks;
        this.maxQuestions = maxQuestions;
        questionCounter = 0;
        askCounter = 0;
    }

    public void init() {
        answerToQuestionsPresenter.setQuestionNumber(maxQuestions - questionCounter);
        answerToQuestionsPresenter.setLayoutVisibility(true);
        askQuestionPresenter.setLayoutVisibility(false);
        answerToQuestionsPresenter.clearAnswerTextEdit();
        answerToQuestionsPresenter.loadRandomQuestion();
        questionCounter ++;
    }

    public void next() {
        if (questionCounter <= maxQuestions) {
            if (validation.validateAnswer(answerToQuestionsPresenter.getCurrentAnswerText())) {
                answerToQuestionsPresenter.sendAnswer();
                answerToQuestionsPresenter.clearAnswerTextEdit();
                answerToQuestionsPresenter.loadRandomQuestion();
                questionCounter ++;
            }
        }
        if (questionCounter > maxQuestions) {
            boolean isValidQuestion = validation
                    .validateQuestion(askQuestionPresenter.getCurrentQuestionText());
            if (askCounter == 0) {
                askCounter = 0;
                answerToQuestionsPresenter.clearAnswerTextEdit();
                answerToQuestionsPresenter.setLayoutVisibility(false);
                askQuestionPresenter.setLayoutVisibility(true);
            } else {
                if (isValidQuestion) {
                    askQuestionPresenter.clearQuestionEditText();
                    if (askCounter < maxAsks) {
                        askQuestionPresenter.sendQuestion();
                        askQuestionPresenter.clearQuestionEditText();
                    } else {
                        askQuestionPresenter.sendQuestion();
                        questionCounter = 1;
                        askCounter = -1;
                        askQuestionPresenter.setLayoutVisibility(false);
                        answerToQuestionsPresenter.setLayoutVisibility(true);
                    }
                }
            }
            askCounter ++;
        }
        answerToQuestionsPresenter.setQuestionNumber(maxQuestions - questionCounter + 1);
    }
}
