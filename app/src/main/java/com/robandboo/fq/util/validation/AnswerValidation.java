package com.robandboo.fq.util.validation;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;

public class AnswerValidation implements IValidation <String> {
    private AppCompatActivity appCompatActivity;

    private int minAnswerSize;

    private int maxAnswerSize;

    private String answerValidationMessage;

    private String answerText;

    public AnswerValidation(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;
        this.answerText = answerText;
        minAnswerSize = appCompatActivity.getResources()
                .getInteger(R.integer.minAnswerSize);
        maxAnswerSize = appCompatActivity.getResources()
                .getInteger(R.integer.maxAnswerSize);
        answerValidationMessage = appCompatActivity.getResources().getString(
                R.string.answerSizeValidationMessageText,
                minAnswerSize,
                maxAnswerSize
        );
    }

    @Override
    public boolean validate() {
        boolean isValid = (answerText != null) && (answerText.length() >= minAnswerSize) &&
                (answerText.length() < maxAnswerSize);
        if (!isValid) {
            Toast.makeText(appCompatActivity, answerValidationMessage, Toast.LENGTH_SHORT)
                    .show();
        }
        return isValid;
    }

    @Override
    public void setDataForValidation(String data) {
        this.answerText = data;
    }

    @Override
    public boolean validateWithoutToast() {
        return (answerText != null) && (answerText.length() >= minAnswerSize) &&
                (answerText.length() < maxAnswerSize);
    }
}
