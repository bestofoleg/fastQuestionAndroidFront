package com.robandboo.fq.callback;

import android.widget.LinearLayout;
import android.widget.Toast;

import com.robandboo.fq.dto.Answer;

import lombok.Builder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Builder
public class SendAnswerCallback implements Callback<Answer> {
    private LinearLayout answerToQuestionLayout;
    private String failureToSendAnswerErrorMessage;

    @Override
    public void onResponse(Call<Answer> call, Response<Answer> response) {
    }

    @Override
    public void onFailure(Call<Answer> call, Throwable t) {
        Toast.makeText(
                answerToQuestionLayout.getContext(),
                failureToSendAnswerErrorMessage,
                Toast.LENGTH_SHORT
        ).show();
        t.printStackTrace();
    }
}
