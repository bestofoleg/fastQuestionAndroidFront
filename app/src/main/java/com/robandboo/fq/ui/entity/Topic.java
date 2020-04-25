package com.robandboo.fq.ui.entity;

import com.robandboo.fq.dto.Answer;
import com.robandboo.fq.dto.Question;

import java.util.List;

public class Topic {
    private Question question;

    private List<Answer> answers;

    public Topic(Question question, List<Answer> answers) {
        this.question = question;
        this.answers = answers;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
