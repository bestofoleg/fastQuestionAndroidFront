package com.robandboo.fq.util.validation;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;

public class QuestionValidation implements IValidation <String>{
    private AppCompatActivity appCompatActivity;

    private int minQuestionSize;

    private int maxQuestionSize;

    private String questionsValidationMessage;

    private String questionText;

    private boolean isVote;

    public QuestionValidation(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        minQuestionSize = appCompatActivity.getResources()
                .getInteger(R.integer.minQuestionSize);
        maxQuestionSize = appCompatActivity.getResources()
                .getInteger(R.integer.maxQuestionSize);
        questionsValidationMessage = appCompatActivity.getResources().getString(
                R.string.questionSizeValidationMessageText,
                minQuestionSize,
                maxQuestionSize
        );
    }

    @Override
    public boolean validate() {
        String trimedQuestionText = questionText.trim();
        boolean isValid = (trimedQuestionText != null) && (trimedQuestionText.length() >= minQuestionSize) &&
                (trimedQuestionText.length() < maxQuestionSize);
        if (!isValid) {
            Toast.makeText(appCompatActivity, questionsValidationMessage, Toast.LENGTH_SHORT)
                    .show();
        }
        return isValid;
    }

    @Override
    public void setDataForValidation(String data) {
        this.questionText = data;
    }

    @Override
    public boolean validateWithoutToast() {
        String trimedQuestionText = questionText.trim();
        return (trimedQuestionText != null) && (trimedQuestionText.length() >= minQuestionSize) &&
                (trimedQuestionText.length() < maxQuestionSize);
    }

    @Override
    public void setDataForValidation(String data, boolean isVote) {
        this.questionText = data;
        this.isVote = isVote;
    }
}
