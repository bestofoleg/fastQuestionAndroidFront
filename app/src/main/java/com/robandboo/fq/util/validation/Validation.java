package com.robandboo.fq.util.validation;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;

public class Validation {
    private AppCompatActivity appCompatActivity;

    private int minQuestionSize;

    private int minAnswerSize;

    private int maxQuestionSize;

    private int maxAnswerSize;

    private String questionsValidationMessage;

    private String answersValidationMessage;

    public Validation(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        minQuestionSize = appCompatActivity.getResources()
                .getInteger(R.integer.minQuestionSize);
        minAnswerSize = appCompatActivity.getResources()
                .getInteger(R.integer.minAnswerSize);
        maxQuestionSize = appCompatActivity.getResources()
                .getInteger(R.integer.maxQuestionSize);
        maxAnswerSize = appCompatActivity.getResources()
                .getInteger(R.integer.maxAnswerSize);
        questionsValidationMessage = appCompatActivity.getResources().getString(
                R.string.questionSizeValidationMessageText,
                minQuestionSize,
                maxQuestionSize
        );
        answersValidationMessage = appCompatActivity.getResources().getString(
                R.string.answerSizeValidationMessageText,
                minAnswerSize,
                maxAnswerSize
        );
    }

    public boolean validateQuestion(String text) {
        boolean isValid = (text.length() >= minQuestionSize) &&
                (text.length() < maxQuestionSize);
        if (!isValid) {
            Toast.makeText(appCompatActivity, questionsValidationMessage, Toast.LENGTH_SHORT)
                    .show();
        }
        return isValid;
    }

    public boolean validateAnswer(String text) {
        boolean isValid = (text.length() >= minAnswerSize) &&
                (text.length() < maxAnswerSize);
        if (!isValid) {
            Toast.makeText(appCompatActivity, answersValidationMessage, Toast.LENGTH_SHORT)
                    .show();
        }
        return text.length() > minAnswerSize;
    }
}
