package com.robandboo.fq.chain;

import com.robandboo.fq.util.activity.LayoutManager;

import java.util.Queue;

public class ChainManager {
    private LayoutManager layoutManager;

    private Queue<String> layoutIdsFlow;

    private int questionsQuantity;

    private int maxQuestionsQuantity;

    private int answersQuantity;

    private int maxAnswersQuantity;

    private QuestionLayoutPresenter questionLayoutPresenter;

    public ChainManager(QuestionLayoutPresenter questionLayoutPresenter) {
        this.questionLayoutPresenter = questionLayoutPresenter;
    }

    public int getQuestionsQuantity() {
        return questionsQuantity;
    }

    public void setQuestionsQuantity(int questionsQuantity) {
        this.questionsQuantity = questionsQuantity;
    }

    public int getMaxQuestionsQuantity() {
        return maxQuestionsQuantity;
    }

    public void setMaxQuestionsQuantity(int maxQuestionsQuantity) {
        this.maxQuestionsQuantity = maxQuestionsQuantity;
    }

    public int getAnswersQuantity() {
        return answersQuantity;
    }

    public void setAnswersQuantity(int answersQuantity) {
        this.answersQuantity = answersQuantity;
    }

    public int getMaxAnswersQuantity() {
        return maxAnswersQuantity;
    }

    public void setMaxAnswersQuantity(int maxAnswersQuantity) {
        this.maxAnswersQuantity = maxAnswersQuantity;
    }

    public void next() {
        if (questionsQuantity < maxQuestionsQuantity) {
            questionLayoutPresenter.loadQuestion();
            questionsQuantity ++;
        } else {
            if (answersQuantity < maxAnswersQuantity) {
                answersQuantity ++;
            } else {
                answersQuantity = 0;
                questionsQuantity = 0;
            }
        }
    }
}
