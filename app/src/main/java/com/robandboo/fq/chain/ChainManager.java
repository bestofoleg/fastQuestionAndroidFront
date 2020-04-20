package com.robandboo.fq.chain;

import com.robandboo.fq.presenter.QuestionPresenter;

public class ChainManager {
    private QuestionPresenter questionLayoutPresenter;

    private int maxQuestions;

    private int maxAnswers;

    private int questionCounter;

    private int answerCounter;

    public ChainManager(QuestionPresenter questionLayoutPresenter, int maxQuestions, int maxAnswers) {
        this.questionLayoutPresenter = questionLayoutPresenter;
        this.maxAnswers = maxAnswers;
        this.maxQuestions = maxQuestions;
        questionCounter = 0;
        answerCounter = 0;
    }

    public void next(boolean isSendAnswer) {
        if (questionCounter < maxQuestions) {
            questionLayoutPresenter.loadRandomQuestion();
            if (isSendAnswer) {
                questionLayoutPresenter.sendAnswer();
            }
            questionLayoutPresenter.clearAnswerTextEdit();
            questionCounter ++;
        } else {
            questionCounter = 0;
            questionLayoutPresenter.setQuestionLayoutVisibility(false);
        }
    }
}
