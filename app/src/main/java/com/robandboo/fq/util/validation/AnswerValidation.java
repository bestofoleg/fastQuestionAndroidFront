package com.robandboo.fq.util.validation;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.robandboo.fq.R;

import lombok.Getter;
import lombok.Setter;

public class AnswerValidation implements IValidation<String> {
    private AppCompatActivity appCompatActivity;

    private int minAnswerSize;

    private int maxAnswerSize;

    private String answerValidationMessage;

    private String answerText;

    @Getter
    @Setter
    private boolean isVote;

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
        String trimedAnswerText = answerText.trim();
        boolean isValid = ((trimedAnswerText != null) && (trimedAnswerText.length() >= minAnswerSize) &&
                (trimedAnswerText.length() < maxAnswerSize));
        if (!isValid) {
            if (!isVote) {
                Toast.makeText(appCompatActivity, answerValidationMessage, Toast.LENGTH_SHORT)
                        .show();
            }
        }
        return isValid;
    }

    @Override
    public void setDataForValidation(String data) {
        this.answerText = data;
    }

    @Override
    public void setDataForValidation(String data, boolean isVote) {
        this.answerText = data;
        this.isVote = isVote;
    }

    @Override
    public boolean validateWithoutToast() {
        String trimedAnswerText = answerText.trim();
        return ((trimedAnswerText != null) && (trimedAnswerText.length() >= minAnswerSize) &&
                (trimedAnswerText.length() < maxAnswerSize));
    }
}
