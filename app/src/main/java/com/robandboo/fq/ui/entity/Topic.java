package com.robandboo.fq.ui.entity;

import com.robandboo.fq.dto.Answer;

import java.util.List;

public class Topic {
    private String questionText;

    private List<Answer> answers;

    public Topic(String questionText, List<Answer> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
