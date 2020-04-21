package com.robandboo.fq.chain;

import com.robandboo.fq.presenter.AskQuestionPresenter;
import com.robandboo.fq.presenter.AnswerToQuestionsPresenter;

public class ChainManager {
    private AnswerToQuestionsPresenter answerToQuestionsPresenter;

    private AskQuestionPresenter askQuestionPresenter;

    private int maxQuestions;

    private int maxAsks;

    private int questionCounter;

    private int askCounter;

    public ChainManager(
            AnswerToQuestionsPresenter answerToQuestionsPresenter,
            AskQuestionPresenter askQuestionPresenter,
            int maxQuestions,
            int maxAsks
    ) {
        this.answerToQuestionsPresenter = answerToQuestionsPresenter;
        this.askQuestionPresenter = askQuestionPresenter;
        this.maxAsks = maxAsks;
        this.maxQuestions = maxQuestions;
        questionCounter = 0;
        askCounter = 0;
    }

    public void init() {
        answerToQuestionsPresenter.setLayoutVisibility(true);
        askQuestionPresenter.setLayoutVisibility(false);
        answerToQuestionsPresenter.clearAnswerTextEdit();
        answerToQuestionsPresenter.loadRandomQuestion();
        questionCounter ++;
    }

    public void next() {
        if (questionCounter < maxQuestions) {
            answerToQuestionsPresenter.sendAnswer();
            answerToQuestionsPresenter.clearAnswerTextEdit();
            answerToQuestionsPresenter.loadRandomQuestion();
            questionCounter ++;
        } else {
            if (askCounter == 0) {
                answerToQuestionsPresenter.clearAnswerTextEdit();
                answerToQuestionsPresenter.setLayoutVisibility(false);
                askQuestionPresenter.setLayoutVisibility(true);
            } else {
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
            askCounter ++;
        }
    }
}
