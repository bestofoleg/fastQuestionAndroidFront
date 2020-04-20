package com.robandboo.fq.chain;

import com.robandboo.fq.presenter.AskPresenter;
import com.robandboo.fq.presenter.QuestionPresenter;

public class ChainManager {
    private QuestionPresenter questionPresenter;

    private AskPresenter askPresenter;

    private int maxQuestions;

    private int maxAsks;

    private int questionCounter;

    private int askCounter;

    public ChainManager(
            QuestionPresenter questionPresenter,
            AskPresenter askPresenter,
            int maxQuestions,
            int maxAsks
    ) {
        this.questionPresenter = questionPresenter;
        this.askPresenter = askPresenter;
        this.maxAsks = maxAsks;
        this.maxQuestions = maxQuestions;
        questionCounter = 0;
        askCounter = 0;
    }

    public void init() {
        questionPresenter.setLayoutVisibility(true);
        askPresenter.setLayoutVisibility(false);
        questionPresenter.clearAnswerTextEdit();
        questionPresenter.loadRandomQuestion();
        questionCounter ++;
    }

    public void next() {
        if (questionCounter < maxQuestions) {
            questionPresenter.sendAnswer();
            questionPresenter.clearAnswerTextEdit();
            questionPresenter.loadRandomQuestion();
            questionCounter ++;
        } else {
            if (askCounter == 0) {
                questionPresenter.clearAnswerTextEdit();
                questionPresenter.setLayoutVisibility(false);
                askPresenter.setLayoutVisibility(true);
            } else {
                if (askCounter < maxAsks) {
                    askPresenter.sendQuestion();
                    askPresenter.clearQuestionEditText();
                } else {
                    askPresenter.sendQuestion();
                    questionCounter = 1;
                    askCounter = -1;
                    askPresenter.setLayoutVisibility(false);
                    questionPresenter.setLayoutVisibility(true);
                }
            }
            askCounter ++;
        }
    }
}
