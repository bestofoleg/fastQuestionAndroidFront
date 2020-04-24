package com.robandboo.fq.util.validation;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;

public class Validation {
    private int minQuestionSize;

    private int minAnswerSize;

    public Validation(AppCompatActivity appCompatActivity) {
        minQuestionSize = appCompatActivity.getResources()
                .getInteger(R.integer.minQuestionSize);
        minAnswerSize = appCompatActivity.getResources()
                .getInteger(R.integer.minAnswerSize);
    }

    public boolean validateQuestion(String text) {
        return text.length() > minQuestionSize;
    }

    public boolean validateAnswer(String text) {
        return text.length() > minAnswerSize;
    }
}
